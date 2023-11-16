package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import javax.inject.Inject

class AccountPresenter @Inject constructor(
    private val authInteractor: AuthInteractor
) {

    suspend fun getUserEmail(): String? = withIOContext {
        return@withIOContext authInteractor.getCurrentUserEmail()
    }

    suspend fun signOut() = withIOContext {
        authInteractor.signOut()
    }

    suspend fun changePassword() = withIOContext {
        authInteractor.sendPasswordReset()
    }

    suspend fun changeEmail(context: Context, password: String?, newEmail: String?) = withIOContext {
        authInteractor.changeEmail(context, password, newEmail)
    }
}