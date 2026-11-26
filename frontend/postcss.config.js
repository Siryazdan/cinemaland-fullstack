// Project-local PostCSS config. Without it, PostCSS searches parent directories
// and can pick up an unrelated config (e.g. one in the user's home folder).
module.exports = {
  plugins: {
    autoprefixer: {},
  },
};
