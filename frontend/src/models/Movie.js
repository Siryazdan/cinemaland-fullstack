export default class Movie {
    constructor(id = null,
                title = '',
                genre = '',
                rating = null,
                cast = '',
                director = '',
                synopsis = '',
    ) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.cast = cast;
        this.director = director;
        this.synopsis = synopsis;
    }
}