<?php

namespace App\Data;

use App\Data\Host\Address;

class Host
{
    // mAddressList
    private $addressList;

    public function __construct()
    {
        $this->addressList = [];
    }

    public function getAddressList()
    {
        return $this->addressList;
    }

    public function setAddressList($addressList)
    {
        if (is_object($addressList))
        {
            if ($addressList instanceof Address)
            {
                $this->addressList[] = $addressList;
            }
        }
        else
        {
            if (is_array($addressList))
            {
                foreach ($addressList as $address)
                {
                    $this->setAddressList($address);
                }
            }
        }
    }

    public static function from($data)
    {
        $hostObject = new Host();

        if (is_array($data))
        {
            if (isset($data['addressList']))
            {
                $addressListData = $data['addressList'];

                if (is_array($addressListData))
                {
                    $addressList = [];

                    foreach ($addressListData as $addressData)
                    {
                        $addressList[] = Address::from($addressData);
                    }

                    $hostObject->setAddressList($addressList);
                }
            }

            return $hostObject;
        }

        return null;
    }

    public static function to($hostObject)
    {
        if (is_object($hostObject))
        {
            if ($hostObject instanceof Host)
            {
                $addressList = $hostObject->getAddressList();
                $addressListData = [];

                foreach ($addressList as $address)
                {
                    $addressListData[] = Address::to($address);
                }

                $data = [
                    'addressList' => $addressListData,
                ];

                return $data;
            }
        }

        return null;
    }
}
