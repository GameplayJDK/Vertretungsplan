<?php

namespace App\Http\Fetcher;

interface Fetcher
{
    public function __construct($data, $debug = false);

    public function fetch($host, $result);
}
