package com.geapfit.utils

import com.exemplio.geapfitmobile.utils.MyUtils


class Path(val path: String)

object StaticNamesPath {
    val passwordGrant = Path(":signInWithPassword")
    val signInCI = Path("/sign_in_auth")
    val refresh = Path("/refresh")
    val authorize = Path("/oauth/authorize")
    val closeSession = Path("/close_session")
    val resendCode = Path("/resend_code")
    val credentials = Path("/oauth/info_from_credentials")
    val profile = Path("/my_profile")
    val getClients = Path("/users")
    val roles = Path("/roles")
    val selfSignUp = Path("/self_sign_up")
    val recover = Path("/recovery_questions")
    val sendRecover = Path("/recovery")
    val resend = Path("/self_sign_up_resend_code")
    val recoverExpired = Path("/password_expired")
    val securityQuestions = Path("/register_security_questions")
    val rate = Path("${MyUtils.type}/currency_exchange_rate/report")
    val createCategory = Path("${MyUtils.type}/inventory_category/create_category")
    val withdraw = Path("${MyUtils.type}/inventory_pay_payment/withdrawal")
}