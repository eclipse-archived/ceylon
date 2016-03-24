var Test5952={
  a:function(){return "Test.a";},
  A:function(){return "Test.A";}
};
Test5952.a.b=function(){return "Test.a.b";};
Test5952.a.B=function(){return {string:"Test.a.B"};};
Test5952.A.b=function(){return {string:"Test.A.b"};};
Test5952.A.B=function(){return {string:"Test.A.B"};};

var $test$5959;
function test5959() {
  if ($test$5959===undefined) {
    $test$5959={
      m1:function(){return "UNO";},
      m2:function(){return "DOS";},
      p1:1,
      p2:2
    };
    $test$5959.a=$test$5959;
    $test$5959.b=$test$5959;
  }
  return $test$5959;
}
function genC5959() {
  return {t:"c5959"};
}
function genD5959() {
  return {t:"d5959",s:1};
}
