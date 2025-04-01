document.addEventListener('DOMContentLoaded', function () {
    const word = document.querySelector('.word');
    const chars = word.querySelectorAll('span');

    // Set character total
    word.style.setProperty('--char-total', chars.length);

    // Set individual character indexes and delays
    chars.forEach((char, index) => {
        char.style.setProperty('--char-index', index);
        char.style.animationDelay = `${index * 0.2}s`;
    });
});
