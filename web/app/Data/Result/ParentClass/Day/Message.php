<?php

namespace App\Data\Result\ParentClass\Day;

class Message implements ListItem
{
    // mFirst
    private $first;

    // mLast
    private $last;

    public function __construct()
    {
        $this->first = null;
        $this->last = null;
    }

    public function getFirst()
    {
        return $this->first;
    }

    public function setFirst($first)
    {
        if (is_string($first))
        {
            $this->first = $first;
        }
    }

    public function getLast()
    {
        return $this->last;
    }

    public function setLast($last)
    {
        if (is_string($last))
        {
            $this->last = $last;
        }
    }

    public static function from($data)
    {
        $messageObject = new Message();

        if (is_array($data))
        {
            if (isset($data['first']))
            {
                $messageObject->setFirst($data['first']);
            }

            if (isset($data['last']))
            {
                $messageObject->setFirst($data['last']);
            }

            return $messageObject;
        }
    }

    public static function to($messageObject)
    {
        if (is_object($messageObject))
        {
            if ($messageObject instanceof Message)
            {
                $data = [
                    'first' => $messageObject->getFirst(),
                    'last' => $messageObject->getLast(),
                ];

                return $data;
            }
        }
    }
}