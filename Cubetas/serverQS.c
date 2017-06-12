#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>//read
#include <stdlib.h>//exit
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()
#include <pthread.h>

// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }

    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

void quicksort(int lista[],int limite_izq,int limite_der)
{
    int izq,der,temporal,pivote;

    izq=limite_izq;
    der = limite_der;
    pivote = lista[(izq+der)/2];

    do{
        while(lista[izq]<pivote && izq<limite_der)izq++;
        while(pivote<lista[der] && der > limite_izq)der--;
        if(izq <=der)
        {
            temporal= lista[izq];
            lista[izq]=lista[der];
            lista[der]=temporal;
            izq++;
            der--;

        }

    }while(izq<=der);
    if(limite_izq<der){quicksort(lista,limite_izq,der);}
    if(limite_der>izq){quicksort(lista,izq,limite_der);}

}

int main(int argc, char* argv[]){
 int sd,n,n1,v=1,rv,op=0, *new_sock, cd;
 socklen_t ctam;
 char s[INET6_ADDRSTRLEN], hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];
 //struct sockaddr_in sdir,cdir;
 struct addrinfo hints, *servinfo, *p;
 struct sockaddr_storage their_addr; // connector's address 
 ctam= sizeof(their_addr);
 memset(&hints, 0, sizeof (hints));  //indicio
 hints.ai_family = AF_INET6;    /* Allow IPv4 or IPv6  familia de dir*/
 hints.ai_socktype = SOCK_STREAM;
 hints.ai_flags = AI_PASSIVE; // use my IP
 hints.ai_protocol = 0;          /* Any protocol */
 hints.ai_canonname = NULL;
 hints.ai_addr = NULL;
 hints.ai_next = NULL;
 printf("%s\n", argv[1]);
 if ((rv = getaddrinfo(NULL, argv[1], &hints, &servinfo)) != 0) {
     fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
     return 1;
 }//if

    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }

        if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &v,sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }

	if (setsockopt(sd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1) {
            perror("setsockopt   no soporta IPv6");
            exit(1);
        }

        if (bind(sd, p->ai_addr, p->ai_addrlen) == -1) {
            close(sd);
            perror("server: bind");
            continue;
        }//if

        break;
    }//for

    freeaddrinfo(servinfo); // all done with this structure

    if (p == NULL)  {
        fprintf(stderr, "servidor: error en bind\n");
        exit(1);
    }

   listen(sd,5);
   printf("Servidor listo.. Esperando clientes \n");
  int i=0;
  for(i=0;i<1;i++){
  
    ctam = sizeof their_addr;
    cd = accept(sd, (struct sockaddr *)&their_addr, &ctam);
    if (cd == -1) {
      perror("accept");
      continue;
    }
    if (getnameinfo((struct sockaddr *)&their_addr, sizeof(their_addr), hbuf, sizeof(hbuf), sbuf,sizeof(sbuf), NI_NUMERICHOST | NI_NUMERICSERV) == 0)
      printf("cliente conectado desde %s:%s\n", hbuf,sbuf);
    FILE* f = fdopen(cd, "w+");
    int temp;
    int received, sent;
    received =  read(cd, &temp, sizeof(temp));
    int temp1 = temp;
    int size_cubeta = ntohl(temp);
    fflush(f);
    printf("Seran recibidos: %d numeros\n", size_cubeta);
    int cubeta[size_cubeta];
    int i;  
    for(i=0; i<size_cubeta; i++){
      received = read(cd, &temp, sizeof(temp));
      cubeta[i] = ntohl(temp);
      fflush(f);
    }
    printf("Ordenando...\n");
    quicksort(cubeta, 0, size_cubeta - 1);
    puts("Enviando...\n");
    for( i=0; i<size_cubeta; i++){
      temp = htonl(cubeta[i]);
      sent = write(cd, &temp, sizeof(temp));
      fflush(f);
    }
    fclose(f);
    close(cd);
    printf("Cliente listo\n");    
   }//for
close(sd);
return 0;
}//main
