package com.blockchain.store.dao.repository;

import android.util.Pair;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.dao.database.model.Vote;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class DaoTransactionRepository {
    private static final String TAG = "DaoTransactionRepositor";

    private static String contractAddress;
    private static String userAddress;
    private static Web3j web3j;
    private static boolean isHasLogs = true;

    private static void init(String contractAddress, String userAddress) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        if (isHasLogs) {
            builder.addInterceptor(interceptor);
        }
        OkHttpClient build = builder.build();
        Web3jService web3jService = new HttpService(BASE_URL_INFURA, build, false);

        DaoTransactionRepository.web3j = Web3jFactory.build(web3jService);
        DaoTransactionRepository.contractAddress = contractAddress;
        DaoTransactionRepository.userAddress = userAddress;
    }

    private static Object decodeFunction(EthCall ethCall, Function function) {
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0) {
            return decode.get(0).getValue();
        } else {
            return "0";
        }
    }

    private static DaoToken decodeDaoToken(EthCall ethCall, Function function) {
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        DaoToken daoToken = new DaoToken();
        try {
            daoToken.address = decode.get(0).getValue().toString();
            daoToken.decimals = Long.valueOf(decode.get(1).getValue().toString());
            daoToken.total = Long.valueOf(decode.get(2).getValue().toString());
        } catch (Exception e) {
            return null;
        }
        return daoToken;
    }

    private static Observable<EthCall> getEthCallObservable(Function dataFunction, String contractAddress) {
        return web3j.ethCall(createEthCallTransaction(DaoTransactionRepository.userAddress, contractAddress, FunctionEncoder.encode(dataFunction)), DefaultBlockParameterName.LATEST).observable();
    }

    public static Observable<Pair<String, String>> getUserData() {
        init(DaoConstants.DAO, AccountManager.getAddress().getHex());
        return Observable.zip(
                getEthCallObservable(getBalance(), DaoConstants.Repository),
                getEthCallObservable(getNotLockedBalance(), DaoConstants.Repository), (balanceCall, getNotLockedBalance) -> {
                    String balance = decodeFunction(balanceCall, getBalance()).toString();
                    String notLockedBalance = decodeFunction(getNotLockedBalance, getBalance()).toString();
                    return new Pair<>(balance, notLockedBalance);
                }
        ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());

    }

    public static Observable<List<DaoToken>> getOnlyPmtToken() {
        init(DaoConstants.DAO, AccountManager.getAddress().getHex());
        List<DaoToken> daoTokens = new ArrayList<>();
        return
                getEthCallObservable(getTokenBalanceOfFunction(), DaoConstants.PlayMarket_token_contract)
                        .map(result -> {
                            DaoToken pmToken = new DaoToken().generatePmToken();
                            pmToken.balance = decodeFunction(result, getTokenBalanceOfFunction()).toString();
                            daoTokens.add(pmToken);
                            return daoTokens;
                        })
                        .flatMap(result -> {
                            ArrayList<Observable<EthCall>> list = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                DaoToken daoToken = result.get(i);
                                list.add(getEthCallObservable(getFund(daoToken.address), DaoConstants.Foundation));
                            }
                            return Observable.from(list).flatMap(getFundResult -> getFundResult).toList();
                        }, Pair::new)
                        .map(result -> {
                            Pair<List<DaoToken>, List<EthCall>> tokenEthCallPair = result;
                            for (int i = 0; i < tokenEthCallPair.first.size(); i++) {
                                tokenEthCallPair.first.get(i).fund = decodeFunction(tokenEthCallPair.second.get(i), getFund(tokenEthCallPair.first.get(i).address)).toString();
                            }
                            return tokenEthCallPair.first;
                        })
                        .flatMap(result -> {
                            ArrayList<Observable<EthCall>> list = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                DaoToken daoToken = result.get(i);
                                list.add(getEthCallObservable(getWithdrawn(daoToken.address), DaoConstants.Foundation));
                            }
                            return Observable.from(list).flatMap(getFundResult -> getFundResult).toList();
                        }, Pair::new).map(result -> {
                    Pair<List<DaoToken>, List<EthCall>> tokenEthCallPair = result;
                    for (int i = 0; i < tokenEthCallPair.first.size(); i++) {
                        tokenEthCallPair.first.get(i).withdraw = decodeFunction(tokenEthCallPair.second.get(i), getWithdrawn(tokenEthCallPair.first.get(i).address)).toString();
                    }
                    return tokenEthCallPair.first;
                })
                        .flatMap(result -> getEthCallObservable(getBalance(), DaoConstants.Repository), Pair::new)
                        .map(result -> {
                            for (int i = 0; i < result.first.size(); i++) {
                                result.first.get(i).daoBalance = decodeFunction(result.second, getNotLockedBalance()).toString();
                            }
                            return result.first;
                        }).flatMap(result -> getEthCallObservable(getNotLockedBalance(), DaoConstants.Repository), Pair::new)
                        .map(result -> {
                            for (int i = 0; i < result.first.size(); i++) {
                                result.first.get(i).daoNotLockedBalance = decodeFunction(result.second, getBalance()).toString();
                            }
                            return result.first;
                        })
                        .flatMap(result -> getEthCallObservable(isWithDrawIsblocked(), DaoConstants.Foundation), Pair::new)
                        .map(result -> {
                            for (int i = 0; i < result.first.size(); i++) {
                                result.first.get(i).isWithdrawBlocked = (boolean) decodeFunction(result.second, isWithDrawIsblocked());
                            }
                            return result.first;
                        })
                        .flatMap(result -> getEthCallObservable(allowance(), DaoConstants.PlayMarket_token_contract), Pair::new)
                        .map(result -> {
                            for (int i = 0; i < result.first.size(); i++) {
                                result.first.get(i).approval = decodeFunction(result.second, allowance()).toString();
                            }
                            return result.first;
                        })
                        .zipWith(TokenRepository.getUserTokens(), Pair::new)
                        .map(result -> {
                            result.first.addAll(result.second);
                            return result.first;
                        })
                        .flatMap(result -> {
                            for (DaoToken daoToken : result) {
                                if (daoToken.address.equalsIgnoreCase(DaoConstants.CRYPTO_DUEL_CONTRACT)) {
                                    return getEthCallObservable(getOwnerbal(), DaoConstants.CRYPTO_DUEL_CONTRACT).subscribeOn(Schedulers.newThread());
                                }
                            }
                            return Observable.just(0);
                        }, Pair::new)
                        .map(pairOfTokensAndEthCall -> {

                            for (DaoToken daoToken : pairOfTokensAndEthCall.first) {
                                if (daoToken.address.equalsIgnoreCase(DaoConstants.CRYPTO_DUEL_CONTRACT) && pairOfTokensAndEthCall.second instanceof EthCall) {
                                    daoToken.ownersBal = decodeFunction((EthCall) pairOfTokensAndEthCall.second, getOwnerbal()).toString();
                                }
                            }
                            return pairOfTokensAndEthCall.first;
                        })

                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());

    }


    public static Observable<List<DaoToken>> getTokens() {
        init(DaoConstants.DAO, AccountManager.getAddress().getHex());
        ArrayList<DaoToken> daoTokens = new ArrayList<>();
        return Observable.range(0, 10000)
                .map(position -> getEthCallObservable(tokens(position), DaoConstants.Foundation))
                .flatMap(daoToken -> daoToken)
                .map(result -> {
                    DaoToken token = decodeDaoToken(result, tokens(0));
                    if (token != null)
                        daoTokens.add(token);
                    return token;
                })
                .takeWhile(daoToken -> daoToken != null)
                .toList()
                .flatMap(DaoTransactionRepository::mapGetTokenNameAndSymbol, Pair::new)
                .map(DaoTransactionRepository::mapTokenWithPair)
                .flatMap(result -> getEthCallObservable(getTokenBalanceOfFunction(), DaoConstants.PlayMarket_token_contract), Pair::new)
                .map(result -> {
                    DaoToken pmToken = new DaoToken().generatePmToken();
                    pmToken.balance = decodeFunction(result.second, getTokenBalanceOfFunction()).toString();
                    result.first.add(0, pmToken);
                    return result.first;
                })
                .flatMap(result -> {
                    ArrayList<Observable<EthCall>> list = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        DaoToken daoToken = result.get(i);
                        list.add(getEthCallObservable(getFund(daoToken.address), DaoConstants.Foundation));
                    }
                    return Observable.from(list).flatMap(getFundResult -> getFundResult).toList();
                }, Pair::new)
                .map(result -> {
                    Pair<List<DaoToken>, List<EthCall>> tokenEthCallPair = result;
                    for (int i = 0; i < tokenEthCallPair.first.size(); i++) {
                        tokenEthCallPair.first.get(i).fund = decodeFunction(tokenEthCallPair.second.get(i), getFund(tokenEthCallPair.first.get(i).address)).toString();
                    }
                    return tokenEthCallPair.first;
                })
                .flatMap(result -> {
                    ArrayList<Observable<EthCall>> list = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        DaoToken daoToken = result.get(i);
                        list.add(getEthCallObservable(getWithdrawn(daoToken.address), DaoConstants.Foundation));
                    }
                    return Observable.from(list).flatMap(getFundResult -> getFundResult).toList();
                }, Pair::new).map(result -> {
                    Pair<List<DaoToken>, List<EthCall>> tokenEthCallPair = result;
                    for (int i = 0; i < tokenEthCallPair.first.size(); i++) {
                        tokenEthCallPair.first.get(i).withdraw = decodeFunction(tokenEthCallPair.second.get(i), getWithdrawn(tokenEthCallPair.first.get(i).address)).toString();
                    }
                    return tokenEthCallPair.first;
                })
                .flatMap(result -> getEthCallObservable(getBalance(), DaoConstants.Repository), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.first.size(); i++) {
                        result.first.get(i).daoBalance = decodeFunction(result.second, getBalance()).toString();
                    }
                    return result.first;
                }).flatMap(result -> getEthCallObservable(getNotLockedBalance(), DaoConstants.Repository), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.first.size(); i++) {
                        result.first.get(i).daoNotLockedBalance = decodeFunction(result.second, getNotLockedBalance()).toString();
                    }
                    return result.first;
                })
                .flatMap(result -> getEthCallObservable(isWithDrawIsblocked(), DaoConstants.Foundation), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.first.size(); i++) {
                        result.first.get(i).isWithdrawBlocked = (boolean) decodeFunction(result.second, isWithDrawIsblocked());
                    }
                    return result.first;
                })
                .flatMap(result -> getEthCallObservable(allowance(), DaoConstants.PlayMarket_token_contract), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.first.size(); i++) {
                        result.first.get(i).approval = decodeFunction(result.second, allowance()).toString();
                    }
                    return result.first;
                })
                .flatMap(result -> RestApi.getServerApi().getBalance(userAddress), Pair::new)
                .map(result -> {
                    AccountManager.setUserBalance(result.second);
                    return result.first;
                })

                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());

    }

    private static List<DaoToken> mapTokenWithPair(Pair<List<DaoToken>, List<Pair<String, String>>> result) {
        for (int i = 0; i < result.first.size(); i++) {
            result.first.get(i).name = result.second.get(i).first;
            result.first.get(i).symbol = result.second.get(i).second;
            result.first.get(i).totalTokensLength = result.first.size();
            result.first.get(i).tokenPositionInArray = i;
        }
        return result.first;
    }

    private static Observable<List<Pair<String, String>>> mapGetTokenNameAndSymbol(List<DaoToken> daoTokenList) {
        ArrayList<Observable<Pair<String, String>>> pricesList = new ArrayList<>();
        for (int i = 0; i < daoTokenList.size(); i++) {
            pricesList.add(TransactionRepository.getTokenNameAndSymbol(daoTokenList.get(i).address));
        }
        return Observable.from(pricesList).flatMap(result -> result).toList();
    }

    public static Observable<List<Proposal>> getProposals(int startId) {
        init(DaoConstants.DAO, AccountManager.getAddress().getHex());
        return Observable.range(startId, 10000)
                .map(position -> new Pair<>(position, getEthCallObservable(proposalFunction(position), DaoConstants.DAO)))
                .flatMap(proposal -> proposal.second.map(result -> new Pair<>(proposal.first, result)))
                //.flatMap(proposal -> proposal.second)
                .map(result -> decodeProposal(result.first, result.second.getValue(), proposalFunction(0)))
                .takeWhile(proposal -> proposal != null)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    public static Observable<Rules> getRules() {
        init(DaoConstants.DAO, AccountManager.getAddress().getHex());
        return Observable
                .zip(
                        getEthCallObservable(minQuorumFunction(), DaoConstants.DAO),
                        getEthCallObservable(debatingPeriodFunction(), DaoConstants.DAO),
                        getEthCallObservable(requisiteMajorityFunction(), DaoConstants.DAO),
                        (minimumQuorum, debatingPeriodDuration, requisiteMajority) -> {
                            Rules rules = new Rules();
                            rules.id = 0;
                            rules.minimumQuorum = Long.parseLong(decodeFunction(minimumQuorum, minQuorumFunction()).toString());
                            rules.debatingPeriodDuration = Long.parseLong(decodeFunction(debatingPeriodDuration, debatingPeriodFunction()).toString());
                            rules.requisiteMajority = Long.parseLong(decodeFunction(requisiteMajority, requisiteMajorityFunction()).toString());
                            return rules;
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }


    /* DAO Repository data*/
    public static Function getVoted(long proposalId, String voter) {/*returns uints*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(proposalId));
        inputParameters.add(new Address(voter));
        return new Function("getVoted", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    public static Function getBalance() {/*returns uints*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(DaoTransactionRepository.userAddress));
        return new Function("getBalance", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    public static Function getNotLockedBalance() {/*returns uints*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(DaoTransactionRepository.userAddress));
        return new Function("getNotLockedBalance", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    public static Function isWithDrawIsblocked() {/*returns uints*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        return new Function("WithdrawIsBlocked", inputParameters, Collections.singletonList(new TypeReference<Bool>() {
        }));
    }

    public static Function allowance() {/*returns uints*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(DaoTransactionRepository.userAddress));
        inputParameters.add(new Address(DaoConstants.Repository));
        return new Function("allowance", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }


    /* DAO Foundation data*/
    public static Function getFund(String tokenAddress) {/* returns uint balance*/
        ArrayList<Type> inputParameters = new ArrayList<>();

        inputParameters.add(new Address(tokenAddress));
//        inputParameters.add(new Address(AccountManager.getAccount().getAddress().getHex()));
        inputParameters.add(new Address(DaoTransactionRepository.userAddress));
        return new Function("getFund", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    public static Function checkWithdrawIsBlock() {
        ArrayList<Type> inputParameters = new ArrayList<>();
        return new Function("WithdrawIsBlocked ", inputParameters, Collections.singletonList(new TypeReference<Bool>() {
        }));
    }

    public static Function getWithdrawn(String tokenAddress) {/* returns uint balance*/
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(tokenAddress));
        inputParameters.add(new Address(DaoTransactionRepository.userAddress));
        return new Function("getWithdrawn", inputParameters, Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    public static Function tokens(int position) {
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(position));

        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Address>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });

        return new Function("Tokens", inputParameters, outputParameters);

    }

    /*ERC 20 */
    public static Function approve(String addressSpender, long tokens) {
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(addressSpender));
        inputParameters.add(new Uint256(tokens));
        return new Function("approve", inputParameters, Collections.singletonList(new TypeReference<Bool>() {
        }));
    }

    private static Function proposalFunction(int position) {

        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(position));

        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Bool>() {
        });
        outputParameters.add(new TypeReference<Bool>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Address>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Bytes32>() {
        });
        outputParameters.add(new TypeReference<Utf8String>() {
        });
        outputParameters.add(new TypeReference<Utf8String>() {
        });
        return new Function("Proposals", inputParameters, outputParameters);
    }

    private static Function minQuorumFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        return new Function("minimumQuorum", new ArrayList<>(), outputParameters);
    }

    private static Function debatingPeriodFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        return new Function("debatingPeriodDuration", new ArrayList<>(), outputParameters);
    }

    private static Function requisiteMajorityFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        return new Function("requisiteMajority", new ArrayList<>(), outputParameters);
    }


    public static Proposal decodeProposal(int position, String data, Function function) {
        List<Type> decode = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        Proposal proposal = new Proposal();
        try {
            proposal.proposalID = position;
            proposal.endTimeOfVoting = Long.parseLong(decode.get(0).getValue().toString());
            proposal.isExecuted = (boolean) decode.get(1).getValue();
            proposal.proposalPassed = (boolean) decode.get(2).getValue();
            proposal.numberOfVotes = Integer.parseInt(decode.get(3).getValue().toString());
            proposal.votesSupport = Long.parseLong(decode.get(4).getValue().toString());
            proposal.votesAgainst = Long.parseLong(decode.get(5).getValue().toString());
            proposal.recipient = decode.get(6).getValue().toString();
            proposal.amount = Long.parseLong(decode.get(7).getValue().toString());
            proposal.description = decode.get(9).getValue().toString();
            proposal.fullDescriptionHash = decode.get(10).getValue().toString();
        } catch (Exception e) {
            return null;
        }
        return proposal;
    }

    private static Function executedProposalFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Bool>() {
        });
        return new Function("Proposal", new ArrayList<>(), outputParameters);

    }

    private static Function voteFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Bool>() {
        });
        outputParameters.add(new TypeReference<Address>() {
        });
        outputParameters.add(new TypeReference<Utf8String>() {
        });

        return new Function("Vote", new ArrayList<>(), outputParameters);

    }

    public static Vote decodeVote(String data) {
        Function function = DaoTransactionRepository.voteFunction();
        List<Type> decode = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        Vote vote = new Vote();
        try {
            vote.proposalID = Integer.parseInt(decode.get(0).getValue().toString());
            vote.position = (boolean) decode.get(1).getValue();
            vote.voter = decode.get(2).getValue().toString();
            vote.justification = decode.get(3).getValue().toString();
        } catch (Exception e) {
            return null;
        }
        return vote;
    }

    private static Function rulesFunction() {
        ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        outputParameters.add(new TypeReference<Uint256>() {
        });
        return new Function("Rules", new ArrayList<>(), outputParameters);
    }

    public static Rules decodeRules(String data) {
        Function function = DaoTransactionRepository.rulesFunction();
        List<Type> decode = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        Rules rules = new Rules();
        try {
            rules.id = 1;
            rules.minimumQuorum = Long.parseLong(decode.get(0).getValue().toString());
            rules.debatingPeriodDuration = Integer.parseInt(decode.get(1).getValue().toString());
            rules.requisiteMajority = Long.parseLong(decode.get(2).getValue().toString());
        } catch (Exception e) {
            return null;
        }
        return rules;
    }

    private static Function getTokenBalanceOfFunction() {
        return new Function("balanceOf", Collections.singletonList(new Address(userAddress)), Collections.singletonList(new TypeReference<Uint>() {
        }));
    }

    /*CryptoDuel*/

    private static Function getOwnerbal() {
        return new Function("ownersbal", Collections.singletonList(new Address(userAddress)), Collections.singletonList(new TypeReference<Uint>() {
        }));
    }


}
