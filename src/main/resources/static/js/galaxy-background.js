document.addEventListener('DOMContentLoaded', function() {
    const galaxyBackground = document.getElementById('galaxy-background');
    const numberOfStars = 100;

    for (let i = 0; i < numberOfStars; i++) {
        const star = document.createElement('div');
        star.classList.add('star');
        star.style.left = `${Math.random() * 100}vw`;
        star.style.top = `${Math.random() * 100}vh`;
        star.style.animationDelay = `${Math.random() * 20}s`;
        galaxyBackground.appendChild(star);
    }
         if (galaxyBackground) {
            galaxyBackground.style.background = 'url(/path/to/your/galaxy-image.jpg) no-repeat center center fixed';
            galaxyBackground.style.backgroundSize = 'cover';
        } else {
            console.error('Element with id "galaxy-background" not found.');
        }
});