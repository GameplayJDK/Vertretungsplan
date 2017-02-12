<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\App;

class MainController extends Controller
{
    private $debug;

    public function __construct()
    {
        $this->debug = App::environment('local');
    }

    public function page()
    {
        $response = view('welcome');
        return $response;
    }

    public function app()
    {
        $url = 'http://play.google.com/store/apps/details?id=' . config('app.store_package_name');

        $response = redirect($url);
        return $response;
    }
}
