document.addEventListener('DOMContentLoaded', function() {
    const cardWrapper = document.querySelector('.card-wrapper');
    const movieItems = document.querySelectorAll('.movie-item');

    // Clone the movie items
    movieItems.forEach(item => {
        const clone = item.cloneNode(true);
        cardWrapper.appendChild(clone);
    });

    // Set the animation
    cardWrapper.style.animation = 'scroll 20s linear infinite';
});
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('movie-modal');
    const btn = document.getElementById('view-all-movies');
    const span = document.getElementsByClassName('close')[0];

    btn.onclick = function() {
        modal.style.display = 'block';
        document.body.classList.add('modal-open');
    }

    span.onclick = function() {
        modal.style.display = 'none';
        document.body.classList.remove('modal-open');
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
            document.body.classList.remove('modal-open');
        }
    }
});
