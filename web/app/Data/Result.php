<?php

namespace App\Data;

use App\Data\Result\ParentClass;

class Result
{
    // mParentClassList
    private $parentClassList;
    
    public function __construct()
    {
        $this->parentClassList = [];
    }

    public function getParentClassList()
    {
        return $this->parentClassList;
    }

    public function setParentClassList($parentClassList)
    {
        if (is_object($parentClassList))
        {
            if ($parentClassList instanceof ParentClass)
            {
                $this->parentClassList[] = $parentClassList;
            }
        }
        else
        {
            if (is_array($parentClassList))
            {
                foreach ($parentClassList as $parentClass)
                {
                    $this->setParentClassList($parentClass);
                }
            }
        }
    }

    public static function from($data)
    {
        $resultObject = new Result();

        if (is_array($data))
        {
            if (isset($data['parentClassList']))
            {
                $parentClassListData = $data['parentClassList'];

                if (is_array($parentClassListData))
                {
                    $parentClassList = [];

                    foreach ($parentClassListData as $parentClassData)
                    {
                        $parentClassList[] = ParentClass::from($parentClassData);
                    }

                    $resultObject->setParentClassList($parentClassList);
                }
            }

            return $resultObject;
        }

        return null;
    }

    public static function to($resultObject)
    {
        if (is_object($resultObject))
        {
            if ($resultObject instanceof Result)
            {
                $parentClassList = $resultObject->getParentClassList();
                $parentClassListData = [];

                foreach ($parentClassList as $parentClass)
                {
                    $parentClassListData[] = ParentClass::to($parentClass);
                }

                $data = [
                    'parentClassList' => $parentClassListData,
                ];

                return $data;
            }
        }

        return null;
    }
}
