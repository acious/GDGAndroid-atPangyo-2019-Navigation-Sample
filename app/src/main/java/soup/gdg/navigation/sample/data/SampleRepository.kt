package soup.gdg.navigation.sample.data

import soup.gdg.navigation.sample.data.model.Movie
import soup.gdg.navigation.sample.data.model.MovieId
import soup.gdg.navigation.sample.data.store.BookmarkDataStore
import soup.gdg.navigation.sample.data.store.MovieDataStore

interface SampleRepository {

    /* Fake Authentication */

    fun signIn()

    fun signOut()

    fun isSignedIn(): Boolean

    /* Fake APIs */

    fun getMovieList(): List<Movie>

    fun getMovieDetail(movieId: MovieId): Movie

    fun getBookmarkMovieList(): List<Movie>

    fun addBookmark(movie: Movie)

    fun removeBookmark(movie: Movie)
}

class SampleRepositoryImpl(
    private val movieStore: MovieDataStore,
    private val bookmarkStore: BookmarkDataStore
) : SampleRepository {

    private var isSignedIn = false

    override fun signIn() {
        isSignedIn = true
    }

    override fun signOut() {
        isSignedIn = false
    }

    override fun isSignedIn(): Boolean {
        return isSignedIn
    }

    override fun getMovieList(): List<Movie> {
        return if (isSignedIn) {
            movieStore.getList().map {
                it.copy(favorite = it.isFavorite())
            }
        } else {
            movieStore.getList()
        }
    }

    override fun getMovieDetail(movieId: MovieId): Movie {
        return if (isSignedIn) {
            movieStore.getDetail(movieId).let {
                it.copy(favorite = it.isFavorite())
            }
        } else {
            movieStore.getDetail(movieId)
        }
    }

    override fun getBookmarkMovieList(): List<Movie> {
        return movieStore.getList()
            .filter { it.isFavorite() }
            .map { it.copy(favorite = true) }
    }

    override fun addBookmark(movie: Movie) {
        bookmarkStore.addBookmark(movie.id)
    }

    override fun removeBookmark(movie: Movie) {
        bookmarkStore.removeBookmark(movie.id)
    }

    private fun Movie.isFavorite(): Boolean {
        return bookmarkStore.isBookmark(id)
    }
}
