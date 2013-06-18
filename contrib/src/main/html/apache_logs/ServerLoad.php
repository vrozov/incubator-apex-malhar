<?php
header("Content-type: application/json");
$redis = new Redis();
$redis->connect('127.0.0.1');
$redis->select(4);
$format = 'YmdHi';
$incr = 60;

// Get from date 
$from = $_GET['from'];
if (!$from || empty($from)) {
  $from  = time()-3600;
}

// get server   
$server = $_GET['server'];

// result array
$result = array();

while ($from < time()) 
{
  $date = gmdate($format, $from);
  if (!$server || empty($server))
  {
    $key =  'm|' . $date . '|*';
    $keys = $redis->getKeys($key);
    foreach($keys as &$val) 
    {
      $arr = $redis->hGetAll($val);
      $result[] = array("timestamp" => $from * 1000, "server" => $val, "view" => $arr[1]);
    }
  } else {
    
    $key =  'm|' . $date . '|0:' . $server;
    //var_dump($key);
    $arr = $redis->hGetAll($key);
    if ($arr)
    {
      $result[] = array("timestamp" => $from * 1000, "server" => $server, "view" => $arr[1]);
    }
  }
  $from += $incr;
}

print json_encode($result);


?>