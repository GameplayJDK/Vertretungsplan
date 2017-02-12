<?php

namespace App\Data\Result\ParentClass\Day;

class Entry implements ListItem
{
    // mFor
    private $for;

    // mHour
    private $hour;

    // mTeacher;
    private $teacher;

    // mSubject
    private $subject;

    // mRoom
    private $room;

    // mInsteadOf
    private $insteadOf;

    // mInfo
    private $info;

    // mType
    private $type;

    public function __construct()
    {
        $this->for = null;
        $this->hour = null;
        $this->teacher = null;
        $this->subject = null;
        $this->room = null;
        $this->insteadOf = null;
        $this->info = null;
        $this->type = null;
    }

    public function getFor()
    {
        return $this->for;
    }

    public function setFor($for)
    {
        if (is_string($for))
        {
            $this->for = $for;
        }
    }

    public function getHour()
    {
        return $this->hour;
    }

    public function setHour($hour)
    {
        if (is_string($hour))
        {
            $this->hour = $hour;
        }
    }

    public function getTeacher()
    {
        return $this->teacher;
    }

    public function setTeacher($teacher)
    {
        if (is_string($teacher))
        {
            $this->teacher = $teacher;
        }
    }

    public function getSubject()
    {
        return $this->subject;
    }

    public function setSubject($subject)
    {
        if (is_string($subject))
        {
            $this->subject = $subject;
        }
    }

    public function getRoom()
    {
        return $this->room;
    }

    public function setRoom($room)
    {
        if (is_string($room))
        {
            $this->room = $room;
        }
    }

    public function getInsteadOf()
    {
        return $this->insteadOf;
    }

    public function setInsteadOf($insteadOf)
    {
        if (is_string($insteadOf))
        {
            $this->insteadOf = $insteadOf;
        }
    }

    function getInfo()
    {
        return $this->info;
    }

    public function setInfo($info)
    {
        if (is_string($info))
        {
            $this->info = $info;
        }
    }

    public function getType()
    {
        return $this->type;
    }

    public function setType($type)
    {
        if (is_string($type))
        {
            $this->type = $type;
        }
    }

    public static function from($data)
    {
        $entryObject = new Entry();

        if (is_array($data))
        {
            if (isset($data['for']))
            {
                $entryObject->setFor($data['for']);
            }

            if (isset($data['hour']))
            {
                $entryObject->setHour($data['hour']);
            }

            if (isset($data['teacher']))
            {
                $entryObject->setTeacher($data['teacher']);
            }

            if (isset($data['subject']))
            {
                $entryObject->setSubject($data['subject']);
            }

            if (isset($data['room']))
            {
                $entryObject->setRoom($data['room']);
            }

            if (isset($data['insteadOf']))
            {
                $entryObject->setInsteadOf($data['insteadOf']);
            }

            if (isset($data['info']))
            {
                $entryObject->setInfo($data['info']);
            }

            if (isset($data['type']))
            {
                $entryObject->setType($data['type']);
            }

            return $entryObject;
        }

        return null;
    }

    public static function to($entryObject)
    {
        if (is_object($entryObject))
        {
            if ($entryObject instanceof Entry)
            {
                $data = [
                    'for' => $entryObject->getFor(),
                    'hour' => $entryObject->getHour(),
                    'teacher' => $entryObject->getTeacher(),
                    'subject' => $entryObject->getSubject(),
                    'room' => $entryObject->getRoom(),
                    'insteadOf' => $entryObject->getInsteadOf(),
                    'info' => $entryObject->getInfo(),
                    'type' => $entryObject->getType(),
                ];

                return $data;
            }
        }

        return null;
    }
}