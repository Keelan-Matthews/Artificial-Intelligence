@set /p version=Please enter your Java Version, Example '19', '18', '8' (without quotes):

javac ./src/*.java --enable-preview --release %version%
java "--enable-preview"  -cp ./bin App 
pause