<?php

namespace App\Data\Result;

use App\Data\Result\ParentClass\Day;

class ParentClass
{
    // mName
    private $name;

    // mCount
    private $count;

    // mDayList
    private $dayList;

    // mDate
    private $date;

    // mWeek
    private $week;

    public function __construct()
    {
        $this->name = null;
        $this->count = null;
        $this->dayList = [];
        $this->date = null;
        $this->week = null;
    }

    public function getName()
    {
        return $this->name;
    }

    public function setName($name)
    {
        if (is_string($name))
        {
            $this->name = $name;
        }
    }

    public function getCount()
    {
        return $this->count;
    }

    public function setCount($count)
    {
        if (is_integer($count))
        {
            $this->count = $count;
        }
    }

    public function getDayList()
    {
        return $this->dayList;
    }

    public function setDayList($dayList)
    {
        if (is_object($dayList))
        {
            if ($dayList instanceof Day)
            {
                $this->dayList[] = $dayList;
            }
        }
        else
        {
            if (is_array($dayList))
            {
                foreach ($dayList as $day)
                {
                    $this->setDayList($day);
                }
            }
        }
    }

    public function getDate()
    {
        return $this->date;
    }

    public function setDate($date)
    {
        if (is_string($date))
        {
            $this->date = $date;
        }
    }

    public function getWeek()
    {
        return $this->week;
    }

    public function setWeek($week)
    {
        if (is_string($week))
        {
            $this->week = $week;
        }
    }

    public static function from($data)
    {
        $parentClassObject = new ParentClass();

        if (is_array($data))
        {
            if (isset($data['name']))
            {
                $parentClassObject->setName($data['name']);
            }

            if (isset($data['count']))
            {
                $parentClassObject->setCount($data['count']);
            }

            if (isset($data['dayList']))
            {
                $dayListData = $data['dayList'];

                if (is_array($dayListData))
                {
                    $dayList = [];

                    foreach ($dayListData as $dayData)
                    {
                        $dayList[] = Day::from($dayData);
                    }

                    $parentClassObject->setDayList($dayList);
                }
            }

            if (isset($data['date']))
            {
                $parentClassObject->setDate($data['date']);
            }

            if (isset($data['week']))
            {
                $parentClassObject->setWeek($data['week']);
            }

            return $parentClassObject;
        }

        return null;
    }

    public static function to($parentClassObject)
    {
        if (is_object($parentClassObject))
        {
            if ($parentClassObject instanceof ParentClass)
            {
                $dayList = $parentClassObject->getDayList();
                $dayListData = [];

                foreach ($dayList as $day)
                {
                    $dayListData[] = Day::to($day);
                }

                $data = [
                    'name' => $parentClassObject->getName(),
                    'count' => $parentClassObject->getCount(),
                    'dayList' => $dayListData,
                    'date' => $parentClassObject->getDate(),
                    'week' => $parentClassObject->getWeek(),
                ];

                return $data;
            }
        }

        return null;
    }
}
