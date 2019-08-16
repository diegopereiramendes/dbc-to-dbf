#include <stdio.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <stdint.h>

#include "blast.h"


#define CHUNK 4096

////////////////////////// dbc2dbf

/* Input file helper function */
static unsigned inf(void *how, unsigned char **buf)
{
    static unsigned char hold[CHUNK];

    *buf = hold;
    return fread(hold, 1, CHUNK, (FILE *)how);
}

/* Output file helper function */
static int outf(void *how, unsigned char *buf, unsigned len)
{
    return fwrite(buf, 1, len, (FILE *)how) != len;
}


void cleanup(FILE* input, FILE* output) {
    if( input  ) fclose(input);
    if( output ) fclose(output);
}

/*
    dbc2dbf(char** input_file, char** output_file)
    This function decompresses a given .dbc input file into the corresponding .dbf.

    Please provide fully qualified names, including file extension.
 */
void dbc2dbf(char input_file[],char output_file[]) {
    FILE          *input = 0, *output = 0;
    int           ret = 0;
    unsigned char rawHeader[2];
    uint16_t      header = 0;
//	char input_file[]="C:\\Users\\Diego\\Desktop\\STMA1601.dbc";
//    char output_file[]="C:\\Users\\Diego\\Desktop\\STMA1601.DBF";


    /* Open input file */
    input  = fopen(input_file, "rb");
    if(input == NULL) {
     //   error("Error reading input file %s: %s", input_file[0], strerror(errno));
    }

    /* Open output file */
    output = fopen(output_file, "wb");
    if(output == NULL) {
        cleanup(input, output);
    //    error("Error reading output file %s: %s", output_file[0], strerror(errno));
    }

    /* Process file header - skip 8 bytes */
    if( fseek(input, 8, SEEK_SET) ) {
        cleanup(input, output);
   //     error("Error processing input file %s: %s", input_file[0], strerror(errno));
    }

    /* Reads two bytes from the header = header size */
    ret = fread(rawHeader, 2, 1, input);
    if( ferror(input) ) {
        cleanup(input, output);
    //    error("Error reading input file %s: %s", input_file[0], strerror(errno));
    }

    /* Platform independent code (header is stored in little endian format) */
    header = rawHeader[0] + (rawHeader[1] << 8);
    
    /* Reset file pointer */
    rewind(input);

    /* Copy file header from input to output */
    unsigned char buf[header];

    ret = fread(buf, 1, header, input);
    if( ferror(input) ) {
        cleanup(input, output);
    //    error("Error reading input file %s: %s", input_file[0], strerror(errno));
    }

    ret = fwrite(buf, 1, header, output);
    if( ferror(output) ) {
        cleanup(input, output);
    //    error("Error writing output file %s: %s", output_file[0], strerror(errno));
    }

    /* Jump to the data (Skip CRC32) */
    if( fseek(input, header + 4, SEEK_SET) ) {
        cleanup(input, output);
     //   error("Error processing input file %s: %s", input_file[0], strerror(errno));
    }

    /* decompress */
    ret = blast(inf, input, outf, output);
    if( ret ) {
        cleanup(input, output);
   //     error("Corrupted file? Blast error code: %d", ret);
    }

    /* see if there are any leftover bytes */
    int n = 0;
    while (fgetc(input) != EOF) n++;
    if (n) {
        cleanup(input, output);
   //     error("blast warning: %d unused bytes of input\n", n);
    }

    cleanup(input, output);
}

int main()
{   
   char input_file[]="C:\\Users\\Diego\\Desktop\\STMA1601.dbc";
   char output_file[]="C:\\Users\\Diego\\Desktop\\STMA1601.DBF";	
   dbc2dbf(input_file,output_file);
   return 0;
}
