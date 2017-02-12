<?php

namespace App\Data\Host;

/**
* 
*/
class Address
{
    // mRealName
    private $realName;

    // mRoute
    private $route;

    function __construct()
    {
        $this->realName = null;
        $this->route = null;
    }

    public function getRealName()
    {
        return $this->realName;
    }

    public function setRealName($realName)
    {
        if (is_string($realName))
        {
            $this->realName = $realName;
        }
    }

    public function getRoute()
    {
        return $this->route;
    }

    public function setRoute($route)
    {
        if (is_string($route))
        {
            if (!starts_with($route, '/'))
            {
                $route = '/' . $route;
            }

            $this->route = $route;
        }
    }

    public static function from($data)
    {
        $addressObject = new Address();

        if (is_array($data))
        {
            if (isset($data['realName']))
            {
                $addressObject->setRealName($data['realName']);
            }

            if (isset($data['route']))
            {
                $addressObject->setRoute($data['route']);
            }

            return $addressObject;
        }

        return null;
    }

    public static function to($addressObject)
    {
        if (is_object($addressObject))
        {
            if ($addressObject instanceof Address)
            {
                $data = [
                    'realName' => $addressObject->getRealName(),
                    'route' => $addressObject->getRoute(),
                ];

                return $data;
            }
        }

        return null;
    }
}
