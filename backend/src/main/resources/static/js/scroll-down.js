document.addEventListener('DOMContentLoaded', function() {
    const moviesButton = document.getElementById('movies-button');
    const allMoviesSection = document.getElementById('movies');

    if (moviesButton && allMoviesSection) {
        moviesButton.addEventListener('click', function(event) {
            event.preventDefault();
            allMoviesSection.scrollIntoView({ behavior: 'smooth' });
        });
    }
});