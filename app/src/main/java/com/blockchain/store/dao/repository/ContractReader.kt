package com.blockchain.store.dao.repository

import com.blockchain.store.dao.data.entities.DaoToken
import com.blockchain.store.dao.ui.DaoConstants
import com.blockchain.store.playmarket.api.RestApi
import com.blockchain.store.playmarket.utilities.AccountManager
import com.blockchain.store.playmarket.utilities.Constants
import kotlinx.coroutines.*
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import rx.subjects.PublishSubject

class ContractReader {

    companion object {

        private val job = SupervisorJob()
        private val scope = CoroutineScope(Dispatchers.Default + job)

        private var web3j: Web3j? = null
        private var userAddress: String? = null

        private fun init() {
            web3j = Web3jFactory.build(HttpService(RestApi.BASE_URL_INFURA))
            userAddress = AccountManager.getAddress()!!.hex
        }

        @JvmStatic
        fun getWalletTokens(publishSubject: PublishSubject<DaoToken>, tokens: ArrayList<DaoToken>) = scope.launch {

            init()

            for (t in tokens) {

                var token = t

                var function = Function("balanceOf", listOf<Type<*>>(Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint>() {}))
                var result = getEthCallResult(function, token.address)

                if (token.address.equals(Constants.PM_TOKEN_ADDRESS, true)) {
                    token = obtainTokenData(token)
                    token.balance = decodeFunction(getEthCallResult(function, DaoConstants.PlayMarket_token_contract), function).toString()
                } else {
                    token.balance = decodeFunction(result, function).toString()
                }

                if (token.address.equals(DaoConstants.CRYPTO_DUEL_CONTRACT, true)) {
                    function = Function("ownersbal", listOf<Type<*>>(Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint>() {}))
                    result = getEthCallResult(function, DaoConstants.CRYPTO_DUEL_CONTRACT)
                    token.ownersBal = decodeFunction(result, function).toString()
                }

                publishSubject.onNext(token)
            }

            publishSubject.onCompleted()
        }

        @JvmStatic
        fun getDividendsTokens(publishSubject: PublishSubject<DaoToken>) = scope.launch {

            init()

            var pmToken = DaoToken().generatePmToken()

            var function = Function("balanceOf", listOf<Type<*>>(Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint>() {}))
            var result = getEthCallResult(function, DaoConstants.PlayMarket_token_contract)
            pmToken.balance = decodeFunction(result, function).toString()
            pmToken = obtainTokenData(pmToken)
            publishSubject.onNext(pmToken)

            for (i in 0..10000) {

                function = Function("Tokens", listOf<Type<*>>(Uint256(i.toLong())), listOf<TypeReference<*>>(object : TypeReference<Address>() {}, object : TypeReference<Uint256>() {}, object : TypeReference<Uint256>() {}))
                result = getEthCallResult(function, DaoConstants.Foundation)
                var token = decodeDaoToken(result, function)

                if (token == null) {
                    publishSubject.onCompleted()
                    break
                }

                function = Function("name", ArrayList<Type<Any>>(), listOf<TypeReference<*>>(object : TypeReference<Utf8String>() {}))
                result = getEthCallResult(function, token.address)
                token.name = decodeFunction(result, function).toString()

                function = Function("symbol", ArrayList<Type<Any>>(), listOf<TypeReference<*>>(object : TypeReference<Utf8String>() {}))
                result = getEthCallResult(function, token.address)
                token.symbol = decodeFunction(result, function).toString()

                token = obtainTokenData(token)
                publishSubject.onNext(token)

            }

            AccountManager.setUserBalance(getUserEthBalance().balance.toString())

        }

        private suspend fun obtainTokenData(token: DaoToken): DaoToken {

            var function = Function("getFund", listOf<Type<*>>(Address(token.address), Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}))
            var result = getEthCallResult(function, DaoConstants.Foundation)
            token.fund = decodeFunction(result, function).toString()

            function = Function("getWithdrawn", listOf<Type<*>>(Address(token.address), Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}))
            result = getEthCallResult(function, DaoConstants.Foundation)
            token.withdraw = decodeFunction(result, function).toString()

            function = Function("getBalance", listOf<Type<*>>(Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint>() {}))
            result = getEthCallResult(function, DaoConstants.Repository)
            token.daoBalance = decodeFunction(result, function).toString()

            function = Function("getNotLockedBalance", listOf<Type<*>>(Address(userAddress)), listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}))
            result = getEthCallResult(function, DaoConstants.Repository)
            token.daoNotLockedBalance = decodeFunction(result, function).toString()

            function = Function("WithdrawIsBlocked", ArrayList<Type<Any>>(), listOf<TypeReference<*>>(object : TypeReference<Bool>() {}))
            result = getEthCallResult(function, DaoConstants.Foundation)
            token.isWithdrawBlocked = decodeFunction(result, function) as Boolean

            function = Function("allowance", listOf<Type<*>>(Address(userAddress), Address(DaoConstants.Repository)), listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}))
            result = getEthCallResult(function, DaoConstants.PlayMarket_token_contract)
            token.approval = decodeFunction(result, function).toString()

            return token

        }

        private suspend fun getEthCallResult(dataFunction: Function, address: String): EthCall = coroutineScope {
            val ethCallTx = Transaction(userAddress, null, null, null, address, null, FunctionEncoder.encode(dataFunction))
            return@coroutineScope withContext(Dispatchers.Default) { web3j!!.ethCall(ethCallTx, DefaultBlockParameterName.LATEST).send() }
        }

        private suspend fun getUserEthBalance(): EthGetBalance = coroutineScope {
            return@coroutineScope withContext(Dispatchers.Default) { web3j!!.ethGetBalance(userAddress, DefaultBlockParameterName.LATEST).send() }
        }

        private fun decodeDaoToken(ethCall: EthCall, function: Function): DaoToken? {
            val decode = FunctionReturnDecoder.decode(ethCall.value, function.outputParameters)
            val daoToken = DaoToken()
            try {
                daoToken.address = decode[0].value.toString()
                daoToken.decimals = java.lang.Long.valueOf(decode[1].value.toString())
                daoToken.total = java.lang.Long.valueOf(decode[2].value.toString())
            } catch (e: Exception) {
                return null
            }

            return daoToken
        }

        private fun decodeFunction(ethCall: EthCall, function: Function): Any {
            val decode = FunctionReturnDecoder.decode(ethCall.value, function.outputParameters)
            return if (decode.size > 0) {
                decode[0].value
            } else {
                "0"
            }
        }

    }

}