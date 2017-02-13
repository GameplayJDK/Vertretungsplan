<?php

namespace App\Http\Fetcher;

use Carbon\Carbon;
use Exception;

use Curl\Curl;
use RefineDom\Document;

use App\Helper\DataHelper;

use App\Data\Result;
use App\Data\Result\ParentClass;
use App\Data\Result\ParentClass\Day;
use App\Data\Result\ParentClass\Day\Entry;
use App\Data\Result\ParentClass\Day\Message;

class EssFetcher implements Fetcher
{
    const URL_FORMAT = '%1$s/%2$02d/%1$s%3$05d.htm';
    const URL_BASE = 'http://www.einhardschule.de/Vertrpl/';

    private $debug;

    private $data;

    private $typeKey;
    private $weekNumber;

    private $authUsername;
    private $authPassword;

    private $ignore;

    private $curl;

    public function __construct($data, $debug = false)
    {
        $this->debug = $debug;

        $this->data = $data;
        $this->dataHelper = new DataHelper();

        $this->typeKey = 'w';
        $this->weekNumber = idate('W');

        $this->authUsername = $this->data['auth']['username'];
        $this->authPassword = $this->data['auth']['password'];

        $this->ignore = $this->data['ignore'];

        $this->motd = $this->data['motd'];

        $this->curl = $this->getCurl();
    }

    private function getCurl()
    {
        if ($this->curl === null)
        {
            $this->curl = new Curl();
            $this->curl->setBasicAuthentication($this->authUsername, $this->authPassword);
        }

        return $this->curl;
    }

    public function fetch($host, $result)
    {
        if (in_array($result, [ 'current', 'next' ], true))
        {
            if ($result === 'next')
            {
                $this->weekNumber++;
            }
        }
        else
        {
            return null;
        }

        $response = $this->dataHelper->getResult($host, $result, !$this->debug);

        if ($response === null)
        {
            $response = $this->getResult();
            $response = $this->dataHelper->getDataAsJson($response);
        }

        $this->dataHelper->setResult($host, $result, $response, !$this->debug);

        return $response;
    }

    public function fetchSingle($typeKey, $weekNumber, $elementIndex)
    {
        $url = self::URL_BASE . vsprintf(self::URL_FORMAT, [ $typeKey, $weekNumber, $elementIndex ]);

        $response = $this->curl->get($url);

        $document = null;

        if ($this->curl->httpStatusCode !== 404) {
            $document = new Document();
            $document->load($response);
        }

        return $document;
    }

    public function getResult()
    {
        $fieldResult = new Result();

        $elementIndex = 0;

        while (($document = $this->fetchSingle($this->typeKey, $this->weekNumber, ++$elementIndex)) !== null)
        {
            $fieldParentClass = new ParentClass();

            $vplInfo = $document->findIndex('font', 0);

            $fieldParentClassName = $vplInfo->findIndex('h2', 0)->text();
            $fieldParentClass->setName($fieldParentClassName);

            $fieldParentClassCount = $elementIndex;
            $fieldParentClass->setCount($fieldParentClassCount);

            $vplTableList = $vplInfo->findIndex('div#vertretung', 0)->find('table');

            $tmpDayNameList = [
                'Montag',
                'Dienstag',
                'Mittwoch',
                'Donnerstag',
                'Freitag',
            ];
            $tmpDayCount = 0;

            $tmpMessageList = [];

            foreach ($vplTableList as $vplTable)
            {
                if ($tmpDayCount === count($tmpDayNameList))
                {
                    break;
                }

                if ($vplTable->attr('class') === 'subst')
                {
                    $fieldDay = new Day();

                    $fieldDayName = $tmpDayNameList[$tmpDayCount];
                    $fieldDay->setName($fieldDayName);

                    $vplTableRowList = $vplTable->find('tr.odd, tr.even');

                    foreach ($vplTableRowList as $vplTableRow)
                    {
                        if (count($vplTableRowColList = $vplTableRow->find('td.list')) === 8)
                        {
                            $fieldEntry = new Entry();

                            $vplTableRowCol = $vplTableRowColList[0]->first('b');
                            $fieldEntryFor = trim($vplTableRowCol->text());
                            $fieldEntry->setFor($fieldEntryFor);

                            $vplTableRowCol = $vplTableRowColList[1];
                            $fieldEntryHour = trim($vplTableRowCol->text());
                            $fieldEntry->setHour($fieldEntryHour);

                            $vplTableRowCol = $vplTableRowColList[2];
                            $fieldEntryTeacher = trim($vplTableRowCol->text());
                            $fieldEntry->setTeacher($fieldEntryTeacher);

                            $vplTableRowCol = $vplTableRowColList[3];
                            $fieldEntrySubject = trim($vplTableRowCol->text());
                            $fieldEntry->setSubject($fieldEntrySubject);

                            $vplTableRowCol = $vplTableRowColList[4];
                            $fieldEntryRoom = trim($vplTableRowCol->text());
                            $fieldEntry->setRoom($fieldEntryRoom);

                            $vplTableRowCol = $vplTableRowColList[5];
                            $fieldEntryInsteadOf = trim($vplTableRowCol->text());
                            $fieldEntry->setInsteadOf($fieldEntryInsteadOf);

                            $vplTableRowCol = $vplTableRowColList[6];
                            $fieldEntryInfo = trim($vplTableRowCol->text());
                            $fieldEntry->setInfo($fieldEntryInfo);

                            $vplTableRowCol = $vplTableRowColList[7];
                            $fieldEntryType = trim($vplTableRowCol->text());
                            $fieldEntry->setType($fieldEntryType);

                            $fieldDay->setEntryList($fieldEntry);
                        }
                    }

                    $fieldDay->setMessageList($tmpMessageList);

                    $fieldParentClass->setDayList($fieldDay);

                    $tmpDayCount++;
                }
                else
                {
                    $vplTableRowList = $vplTable->find('tr');

                    foreach ($vplTableRowList as $vplTableRow)
                    {
                        if (count($vplTableRowColList = $vplTableRow->find('td')) === 2)
                        {
                            $fieldMessage = new Message();

                            $vplTableRowCol = $vplTableRowColList[0];
                            $fieldMessageFirst = $vplTableRowCol->text();
                            $fieldMessage->setFirst($fieldMessageFirst);

                            $vplTableRowCol = $vplTableRowColList[1];
                            $fieldMessageLast = $vplTableRowCol->text();
                            $fieldMessage->setLast($fieldMessageLast);

                            $tmpMessageList[] = $fieldMessage;
                        }
                    }
                }
            }

            $vplInfo = $document->findIndex('font', 1);

            $tmpInfo = trim($vplInfo->text());
            $tmpInfo = explode(' ', $tmpInfo);

            $fieldParentClassDate = (count($tmpInfo) > 0) ? $tmpInfo[0] : '';

            if (!empty($fieldParentClassDate))
            {
                try
                {
                    $carbonDate = Carbon::parse($fieldParentClassDate);
                    $carbonDate->subDays(4);
                    $carbonDate = $carbonDate->format('d.m.Y');

                    $fieldParentClassDate = $carbonDate;
                }
                catch (Exception $ex)
                {
                    $fieldParentClassDate = '';
                }
            }

            $fieldParentClass->setDate($fieldParentClassDate);

            $fieldParentClassWeek = (count($tmpInfo) > 1) ? $tmpInfo[1] : '';
            $fieldParentClass->setWeek($fieldParentClassWeek);

            if ($this->ignore($fieldParentClassName))
            {
                continue;
            }
            else
            {
                $fieldResult->setParentClassList($fieldParentClass);
            }
        }

        $fieldResult = Result::to($fieldResult);

        return $fieldResult;
    }

    public function ignore($name)
    {
        return in_array($name, $this->ignore);
    }
}
