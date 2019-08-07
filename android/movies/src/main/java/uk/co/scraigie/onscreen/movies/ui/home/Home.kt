package uk.co.scraigie.onscreen.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_item.view.*
import org.koin.android.ext.android.inject
import uk.co.scraigie.onscreen.core.framework.*
import uk.co.scraigie.onscreen.core.framework.behaviors.PresenterBehavior
import uk.co.scraigie.onscreen.core_android.behavior.BehaviorFragment
import uk.co.scraigie.onscreen.movies.R
import uk.co.scraigie.onscreen.movies.domain.MoviesInteractor
import uk.co.scraigie.onscreen.movies.ui.load

interface MoviesHomeView : MviView<MoviesHomeIntents, MoviesHomeState>

class MoviesHomeFragment: BehaviorFragment(), MoviesHomeView {

    private val presenter: MoviesHomePresenter by inject()

    init {
        addBehavior(PresenterBehavior(this) { presenter })
    }

    override val intentObservable: Observable<MoviesHomeIntents>
        get() = Observable.empty() //TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun render(state: MoviesHomeState) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies_home_rv.adapter = MoviesHomeAdapter()
    }
}

class MoviesHomeAdapter: RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.itemView.apply {
            content_image.load("https://image.tmdb.org/t/p/w500/kZv92eTc0Gg3mKxqjjDAM73z9cy.jpg")
            title.text = "Movie Title"
            director.text = "Movie Director"
            cast.text = "Brad Pitt, Leonardo DiCaprio"
            rating.text = "4.5"
            genres_chips.apply {
                removeAllViews()
                listOf("Action", "Drama", "Horror").forEach { chipText ->
                    addView((LayoutInflater.from(context).inflate(
                        R.layout.view_text_chip, genres_chips, false) as Chip).apply {
                        this.text = chipText
                    })
                }
            }

        }
    }
}

class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false))

class MoviesHomePresenter constructor(val moviesInteractor: MoviesInteractor): BasePresenter<MoviesHomeView, MoviesHomeState, MoviesHomeIntents, MoviesHomeActions, MoviesHomeResult>() {

    override val intentActionResolver = IntentActionResolver<MoviesHomeIntents, MoviesHomeActions> {
        when(it)  {
            is MoviesHomeIntents.InitialIntent -> MoviesHomeActions.LoadHomeAction
            is MoviesHomeIntents.RefreshIntent -> MoviesHomeActions.LoadHomeAction
        }
    }

    override val actionsProcessor = ActionsProcessor<MoviesHomeActions, MoviesHomeResult> {
            listOf(
                it.addProcessor<MoviesHomeActions.LoadHomeAction>(loadHomeProcessor),
                it.addProcessor<MoviesHomeActions.RefreshAction>(refreshProcessor)
            )
        }

    override val initialState: MoviesHomeState
        get() = MoviesHomeState()

    override val reducer = MviReducer<MoviesHomeState, MoviesHomeResult> {
            previousState, result -> previousState.copy(property = true)
    }

    private val loadHomeProcessor = Processor {
        it.switchMap { moviesInteractor.getHomeContent() }
            .map { MoviesHomeResult.LoadHomeResult }
    }

    private val refreshProcessor = Processor {
        it.map { MoviesHomeResult.RefreshResult }
    }
}

sealed class MoviesHomeIntents : MviIntent {
    object InitialIntent : MoviesHomeIntents()
    object RefreshIntent : MoviesHomeIntents()
}

sealed class MoviesHomeActions : MviAction {
    object LoadHomeAction: MoviesHomeActions()
    object RefreshAction: MoviesHomeActions()
}

sealed class MoviesHomeResult: MviResult {
    object LoadHomeResult : MoviesHomeResult()
    object RefreshResult: MoviesHomeResult()
}



data class MoviesHomeState(val property: Boolean = true) : MviState