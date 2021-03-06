package co.uk.scraigie.onscreen.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import uk.co.scraigie.onscreen.core.framework.*
import uk.co.scraigie.onscreen.tv_shows.R

interface TvHomeView : MviView<TvHomeIntents, TvHomeState>

class TvHomeFragment: Fragment(), TvHomeView {

    override val intentObservable: Observable<TvHomeIntents>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun render(state: TvHomeState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tv_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}



class TvHomePresenter: BaseMviPresenter<TvHomeView, TvHomeState, TvHomeIntents, TvHomeActions, TvHomeResult>() {

    override val intentActionResolver = mapOf(
            TvHomeIntents.InitialIntent to TvHomeActions.LoadHomeAction,
            TvHomeIntents.RefreshIntent to TvHomeActions.LoadHomeAction
    )

    override val actionsProcessor = ActionsProcessor {
        listOf(
            it.addProcessor(loadHomeProcessor),
            it.addProcessor(refreshProcessor)
        )
    }

    override val initialState: TvHomeState
        get() = TvHomeState()

    override val reducer = MviReducer {
            previousState, result -> initialState.copy(property = true)
    }

    private val loadHomeProcessor =SequentialProcessor<TvHomeActions.LoadHomeAction, TvHomeResult.LoadHomeResult> {
        TvHomeResult.LoadHomeResult
    }

    private val refreshProcessor = SequentialProcessor<TvHomeActions.RefreshAction, TvHomeResult.RefreshResult> {
        TvHomeResult.RefreshResult
    }
}

sealed class TvHomeIntents : MviIntent {
    object InitialIntent : TvHomeIntents()
    object RefreshIntent : TvHomeIntents()
}

sealed class TvHomeActions : MviAction {
    object LoadHomeAction: TvHomeActions()
    object RefreshAction: TvHomeActions()
}

sealed class TvHomeResult: MviResult {
    object LoadHomeResult : TvHomeResult()
    object RefreshResult: TvHomeResult()
}



data class TvHomeState(val property: Boolean = true) : MviState