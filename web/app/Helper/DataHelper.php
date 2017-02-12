<?php

namespace App\Helper;

use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Cache;

use App\Data\Host;
use App\Data\Host\Address;
use App\Data\Result;
use App\Data\Result\ParentClass;

class DataHelper
{
    const STORAGE_DISK_NAME = 'json';
    const CACHE_STORE_NAME = 'json';

    const JSON_FILE_EXTENSION = '.json';

    const DECODER_ASSOC = true;
    const DECODER_DEPTH = 512;
    const DECODER_OPTIONS = 0;

    const ENCODER_OPTIONS = 0 | JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_NUMERIC_CHECK;
    const ENCODER_DEPTH = 512;

    private $debug;

    private $filesystem;
    private $cache;

    public function __construct($debug = false)
    {
        $this->debug = $debug;

        $this->filesystem = Storage::disk(self::STORAGE_DISK_NAME);
        $this->cache = Cache::store(self::CACHE_STORE_NAME);
    }

    public function isSpaceless($string)
    {
        return strpos(' ', $string) === false;
    }

    public function areSpaceless($stringList)
    {
        $spaceless = true;

        foreach ($stringList as $string)
        {
            $spaceless = $spaceless && $this->isSpaceless($string);
        }

        return $spaceless;
    }

    public function isCached($key)
    {
        return $this->cache->has($key);
    }

    public function getCached($key)
    {
        return $this->cache->get($key);
    }

    public function setCached($key, $value)
    {
        $this->cache->put($key, $value, 10);
    }

    public function getJsonAsData($json)
    {
        $data = json_decode($json, self::DECODER_ASSOC, self::DECODER_DEPTH, self::DECODER_OPTIONS);

        return $data;
    }

    public function getDataAsJson($data)
    {
        $json = json_encode($data, self::ENCODER_OPTIONS, self::ENCODER_DEPTH);

        return $json;
    }

    public function checkHostData($hostData)
    {
        $hostObject = Host::from($hostData);
        $hostData = Host::to($hostObject);

        return $hostData;
    }


    public function diffHostData($hostData)
    {
        $hostDataCheck = $this->checkHostData($hostData);
        $hostData = array_diff_key($hostData, $hostDataCheck);

        return $hostData;
    }

    public function createHostData()
    {
        $addressListData = [];

        $addressJsonFileList = $this->filesystem->files();
        foreach ($addressJsonFileList as $addressJsonFile)
        {
            $addressData = $this->createAddressData($addressJsonFile);

            if (empty($addressData))
            {
                continue;
            }

            $addressListData[] = $addressData;
        }

        $hostData = [
            'addressList' => $addressListData,
        ];

        return $hostData;
    }

    public function getHost($useCache = true)
    {
        $key = 'host';

        if ($this->isCached($key) && $useCache)
        {
            $hostJson = $this->getCached($key);

            $hostData = $this->getJsonAsData($hostJson);
            $hostData = $this->checkHostData($hostData);
            $hostJson = $this->getDataAsJson($hostData);

            return $hostJson;
        }
        else
        {
            $hostData = $this->createHostData();
            $hostData = $this->checkHostData($hostData);
            $hostJson = $this->getDataAsJson($hostData);

            if ($useCache)
            {
                $this->setCached($key, $hostJson);
            }

            return $hostJson;
        }
    }

    public function checkAddressData($addressData)
    {
        $addressObject = Address::from($addressData);
        $addressData = Address::to($addressObject);

        return $addressData;
    }

    public function diffAddressData($addressData)
    {
        $addressDataCheck = $this->checkAddressData($addressData);
        $addressData = array_diff_key($addressData, $addressDataCheck);

        return $addressData;
    }

    public function createAddressData($addressJsonFile)
    {
        if (ends_with($addressJsonFile, self::JSON_FILE_EXTENSION))
        {
            if ($this->filesystem->exists($addressJsonFile))
            {
                $addressJson = $this->filesystem->get($addressJsonFile);
                $addressData = $this->getJsonAsData($addressJson);

                return $addressData;
            }
            else
            {
                return [];
            }
        }
        else
        {
            return [];
        }
    }

    public function getAddress($host, $useCache = true)
    {
        $key = 'host-' . $host;

        if ($this->isCached($key) && $useCache)
        {
            $addressJson = $this->getCached($key);

            $addressData = $this->getJsonAsData($addressJson);
            $addressData = $this->checkAddressData($addressData);
            $addressJson = $this->getDataAsJson($addressData);

            return $addressJson;
        }
        else
        {
            $addressJsonFile = $host . self::JSON_FILE_EXTENSION;

            $addressData = $this->createAddressData($addressJsonFile);

            if (empty($addressData))
            {
                return null;
            }
            else
            {
                $addressData = $this->checkAddressData($addressData);
                $addressJson = $this->getDataAsJson($addressData);

                if ($useCache)
                {
                    $this->setCached($key, $addressJson);
                }

                return $addressJson;
            }
        }
    }

    public function getAddressExtra($host)
    {
        $addressJsonFile = $host . self::JSON_FILE_EXTENSION;

        $addressData = $this->createAddressData($addressJsonFile);

        if (empty($addressData))
        {
            return null;
        }
        else
        {
            $addressData = $this->diffAddressData($addressData);

            return $addressData;
        }
    }

    public function getResult($host, $result, $useCache = true)
    {
        $key = 'host-' . $host . '-result-' . $result;

        if ($this->isCached($key) && $useCache)
        {
            $resultJson = $this->getCached($key);

            $resultData = $this->getJsonAsData($resultJson);
            $resultData = $this->checkResultData($resultData);
            $resultJson = $this->getDataAsJson($resultData);

            return $resultJson;
        }
        else
        {
            return null;
        }
    }

    public function setResult($host, $result, $resultData, $useCache = true)
    {
        $key = 'host-' . $host . '-result-' . $result;

        if (!$this->isCached($key) && $useCache)
        {
            $resultData = Result::from($resultData);

            $this->setCached($key, $resultData);
        }
    }
}
