document.addEventListener('DOMContentLoaded', function () {
    const slides = document.querySelectorAll('#image-slider .slides img');
    let currentIndex = 0;

    function showNextSlide() {
        document.querySelector('#image-slider .slides').style.transform = `translateX(-${currentIndex * 100}%)`;
        currentIndex = (currentIndex + 1) % (slides.length);
    }

    setInterval(showNextSlide, 3000); // Change image every 3 seconds
});