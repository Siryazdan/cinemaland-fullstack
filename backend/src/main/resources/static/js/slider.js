document.addEventListener('DOMContentLoaded', function () {
    const slides = document.querySelector('.slides');
    const images = slides.querySelectorAll('img');
    let currentIndex = 0;

    function showSlide(index) {
        if (index >= images.length) {
            currentIndex = 0;
        } else if (index < 0) {
            currentIndex = images.length - 1;
        } else {
            currentIndex = index;
        }
        slides.style.transform = `translateX(-${currentIndex * 100}%)`;
    }

    function nextSlide() {
        showSlide(currentIndex + 1);
    }

    function prevSlide() {
        showSlide(currentIndex - 1);
    }

    setInterval(nextSlide, 3000);

    // click listener for right and left buttons
    document.querySelector('#image-slider .nav-button.left').addEventListener('click', prevSlide);
    document.querySelector('#image-slider .nav-button.right').addEventListener('click', nextSlide);

    // click listener for each image
    images.forEach(image => {
        image.addEventListener('click', function () {
            const movieId = this.getAttribute('data-movie-id');
            window.location.href = `/movie/${movieId}`;
        });
    });
});