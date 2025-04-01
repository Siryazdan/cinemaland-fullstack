// Language selector functionality
document.addEventListener('DOMContentLoaded', function() {
    const langSelector = document.querySelector('.language-selector');
    const selectedLang = document.querySelector('.selected-lang span');
    const langOptions = document.querySelectorAll('.lang-dropdown li');

    langOptions.forEach(option => {
        option.addEventListener('click', function() {
            const lang = this.getAttribute('data-lang');
            selectedLang.textContent = lang.toUpperCase();

            // Here you would add your actual language change logic
            console.log('Language changed to:', lang);
        });
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', function(e) {
        if (!langSelector.contains(e.target)) {
            document.querySelector('.lang-dropdown').style.opacity = '0';
            document.querySelector('.lang-dropdown').style.visibility = 'hidden';
        }
    });
});