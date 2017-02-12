<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\App;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Cache;

use App\Helper\DataHelper;
use App\Helper\FetchHelper;

class VertretungsplanController extends Controller
{
    private $debug;

    private $dataHelper;
    private $fetchHelper;

    public function __construct()
    {
        $this->debug = App::environment('local');

        $this->dataHelper = new DataHelper($this->debug);
        $this->fetchHelper = new FetchHelper($this->debug);
    }

    public function host($host = null)
    {
        $content = null;
        $statusCode = 404;

        if ($host === null)
        {
            $content = $this->dataHelper->getHost();
            $statusCode = 200;
        }
        else
        {
            if ($this->dataHelper->isSpaceless($host))
            {
                $content = $this->dataHelper->getAddress($host);

                if ($content !== null)
                {
                    $statusCode = 200;
                }
            }
        }

        $response = response($content, $statusCode);
        $response->header('Content-Type', 'text/plain');
        return $response;
    }

    public function result($host, $result)
    {
        $content = null;
        $statusCode = 404;

        if ($this->dataHelper->areSpaceless([ $host, $result ]))
        {
            $extra = $this->dataHelper->getAddressExtra($host);

            $fetcher = $this->fetchHelper->getFetcherInstance($host, $extra);

            if ($fetcher !== null)
            {
                $content = $fetcher->fetch($host, $result);

                if ($content !== null)
                {
                    $statusCode = 200;
                }
            }
        }

        $response = response($content, $statusCode);
        $response->header('Content-Type', 'text/plain');
        return $response;
    }
}