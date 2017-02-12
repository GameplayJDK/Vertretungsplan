<?php

namespace App\Data\Result\ParentClass;

use App\Data\Result\ParentClass\Day\Entry;
use App\Data\Result\ParentClass\Day\Message;

class Day
{
    // mName
    private $name;

    // mEntryList
    private $entryList;

    // mMessageList
    private $messageList;


    public function __construct()
    {
        $this->name = null;
        $this->entryList = [];
        $this->messageList = [];
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

    public function getEntryList()
    {
        return $this->entryList;
    }

    public function setEntryList($entryList)
    {
        if (is_object($entryList))
        {
            if ($entryList instanceof Entry)
            {
                $this->entryList[] = $entryList;
            }
        }
        else
        {
            if (is_array($entryList))
            {
                foreach ($entryList as $entry)
                {
                    $this->setEntryList($entry);
                }
            }
        }
    }

    public function getMessageList()
    {
        return $this->messageList;
    }

    public function setMessageList($messageList)
    {
        if (is_object($messageList))
        {
            if ($messageList instanceof Message)
            {
                $this->messageList[] = $messageList;
            }
        }
        else
        {
            if (is_array($messageList))
            {
                foreach ($messageList as $message)
                {
                    $this->setMessageList($message);
                }
            }
        }
    }

    public static function from($data)
    {
        $dayObject = new Day();

        if (is_array($data))
        {
            if (isset($data['name']))
            {
                $dayObject->setName($data['name']);
            }

            if (isset($data['entryList']))
            {
                $entryListData = $data['entryList'];

                if (is_array($entryListData))
                {
                    $entryList = [];

                    foreach ($entryListData as $entryData)
                    {
                        $entryList[] = Entry::from($entryData);
                    }

                    $dayObject->setEntryList($entryListData);
                }
            }

            if (isset($data['messageList']))
            {
                $messageListData = $data['messageList'];

                if (is_array($messageListData))
                {
                    $messageList = [];

                    foreach ($messageListData as $messageData)
                    {
                        $messageList[] = Message::from($messageData);
                    }

                    $dayObject->setMessageList($messageListData);
                }
            }

            return $dayObject;
        }

        return null;
    }

    public static function to($dayObject)
    {
        if (is_object($dayObject))
        {
            if ($dayObject instanceof Day)
            {
                $entryList = $dayObject->getEntryList();
                $entryListData = [];

                foreach ($entryList as $entry)
                {
                    $entryListData[] = Entry::to($entry);
                }

                $messageList = $dayObject->getMessageList();
                $messageListData = [];

                foreach ($messageList as $message)
                {
                    $messageListData[] = Message::to($message);
                }

                $data = [
                    'name' => $dayObject->getName(),
                    'entryList' => $entryListData,
                    'messageList' => $messageListData,
                ];

                return $data;
            }
        }

        return null;
    }
}
