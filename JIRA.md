1. Poprawic sidebar grubosci moze byc grubsze na plenym i cienszy na zwinietym
2 chyba na mniejszym rozmiarze jest sidbar z automatu otwarty a nie powinein byc

4.cos jest nie tak z handloawnie errorow bo np w statytyskach na charcie miaelm npe a return jest it tak 200 xd

5. nie wiem czy to zrobie teraz ale jak wciskamy odbieranie zamowien to po otworzeniu nowego okna juz tego nie odbieramy no i pytanie 
jak to zroic zeby sie na dwoch oknach odbieralo no bo jakbym mial pinga to ping tez podwojnie bedzie jebal moglbym zpaisac to w ciasteczkach
ze jest odbieranie ale to wciaz nie daje pelnej spojnosci miedzy oknami ;/

6. jesli ktos zostawi okienko nie ruszane i ten czas przeleci do konca i okienka sie zestackuja to i tak sie beda wyswielac z czasem 0
i odrazu znikac ;/


7. Zrobic jakis komunikat jak odbieramy ordery i checemy przejsc na inna firme to raczej odbieranie orderow powinno sie wyjebac

8. pamietac o dorobieniu sprawdzen uprawnien uzytkonika do firm na endpointach
###########

ta rura robi resiza jak sie wyswielta caly opis xd
![img_1.png](img_1.png)
(prime ng ma tooltipa fjanie bedzie go uzyc)
ty chyba w zaleznosci od thema inaczej te rzeczy reaguja xdddd fluent-light
########


double request po przejsciu i wroceniu na zakladke live panel
![img_2.png](img_2.png)

###
upewnic sie ze wszystkie observable sa niszczone na ngOnDestroy (z socketem jest okej?)

####
Nie ma 400 500 jak nulle leca znowu jakies gowno w tym catchu
![img.png](img.png)

####

czy w paylodzie powinno leciec widoczne haslo przy rejsetaracji i chyba logowaniu?

####

![img_3.png](img_3.png)

cos z tym soundListenerem


####

no trzeba by pomyslec jak to zrobic na front endzie jesli websocket z backendu sie wyjebal ;/


###

trzeba by potestowac tego socketa na 2 uzytkowniach jdnoczesnie, jeden uzytkownik na dowch okienkach wyglda ze dziala wiec not bad


####

mozna by pomyslec o zrobieniu na froncie diaglgu kiedy user ma wlaczone odbieranie zamowien i zmienia firme


#####

pytanie czy ja musze cos robic kiedy otrzymam ordera na backendzie a front nic nie kliknie wiec order powinine zostac odrzucony 
ale nie mam wgle logiki ktora wysle cos dlo glovo wydaje mi sie ze nie musze nic miec ale to upewnienia sie w przyszlosci

###

moze byc case gdzie jedno okno ma 2 queued orders i drugi okno zobaczy najnowszego 3 ordera wiec pierwsze okno nie moze akceptowac/rejectowac
tego zamowienia xd do pomyslenia czy nie lepiel byloby dac tylko jednemu userowi z calej firmy w danym czase prawo do odbierania zamowien

PERMISSIONS

VIEW_ORDERS - prawo do podgladu zakladki Zamówienia oraz wszysktich endpointow zwiazanych z nim
VIEW_STATISTICS - prawo do podgladu zakladki Statystyki oraz wszysktich endpointow zwiazanych z nim
VIEW_LIVE_PANEL - prawo do podgladu zakladki Panel Live oraz wszysktich endpointow zwiazanych z nim
