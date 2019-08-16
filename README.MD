** Não é necessário, mas a caráter de conhecimento, como compilar o código em C e gerar uma biblioteca ".so" para LINUX **
   
   - DbcToDbf/fonteC
   
    $gcc -c -fPIC blast.c
    $gcc -c -fPIC main.c
    $gcc -o blast_linux.so -shared blast.o main.o