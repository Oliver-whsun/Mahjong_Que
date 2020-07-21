@echo off
setLocal EnableDelayedExpansion
set CLASSPATH="
for /R ../../jar %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

echo ================================

java -cp %CLASSPATH% pers.weihengsun.mahjong.game.server
pause