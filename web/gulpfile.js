const elixir = require('laravel-elixir');

/*
 |--------------------------------------------------------------------------
 | Elixir Asset Management
 |--------------------------------------------------------------------------
 |
 | Elixir provides a clean, fluent API for defining some basic Gulp tasks
 | for your Laravel application. By default, we are compiling the Sass
 | file for our application, as well as publishing vendor resources.
 |
 */

elixir(function(mix) {
    mix.copy('node_modules/jquery/dist/jquery.js', 'resources/assets/js/jquery.js');
    mix.copy('node_modules/bootstrap-sass/assets/javascripts/bootstrap.js', 'resources/assets/js/bootstrap.js');
    mix.scripts([ 'jquery.js', 'bootstrap.js', 'app.js' ], 'public/js/app.js');

    mix.sass('app.scss');
});
