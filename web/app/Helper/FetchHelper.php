<?php

namespace App\Helper;

class FetchHelper
{
    const FETCHER_NAME_FORMAT = '%1$s%2$sFetcher';

    const FETCHER_NAMESPACE = '\\App\\Http\\Fetcher\\';

    private $debug;

    public function __construct($debug = false)
    {
        $this->debug = $debug;
    }

    public function getFetcherInstance($host, $extra = [])
    {
        $fetcher = ucfirst($host);
        $fetcher = vsprintf(self::FETCHER_NAME_FORMAT, [ self::FETCHER_NAMESPACE, $fetcher ]);

        if (class_exists($fetcher))
        {
            $fetcherInstance = new $fetcher($extra, $this->debug);

            return $fetcherInstance;
        }
        else
        {
            return null;
        }
    }
}
