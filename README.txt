Stan Alin 332CB
    Initial am inceput prin intelegerea algoritmilor din tema,prin acele tabele din pdf si pe foaie,iar apoi i-am implementat
in myDispatcher,folosind formulele din enuntul temei.
in clasa MyHost am inceput prin initializarea unei Priority Blocking Queue,prin care gestionez mai eficient task-urile.
variabilele volatile workLeft si tasks reprezinta munca totala ramasa si numarul de task-uri din coada, acestea fiind
actualizate in addTask,cand se adauga un task in coada se incrementeaza numarul de task-uri si se actualizeaza workLeft
si sunt folosite pentru metodele getQueueSize si getWorkLeft, care returneaza tasks ,respectiv workLeft.
Metoda shutdown() seteaza flagul shutdownRequested la true, solicitand astfel inchiderea thread-ului.
Metoda simulateTaskExecution simuleaza executia unui task, verifica daca task-ul curent poate fi preemptat de alt task
cu prioritate mai mare din coada,daca este cazul adauga task-ul curent inapoi in coada,altfel se da sleep 1 secunda
si actualizeaza timpul ramas pt task.
in aceasta metoda am folosit si clasa TaskPriorityComparator care implementeaza interfata Comparator.
scopul principal al acestei clase este de a compara task-urile in functie de prioritati,si in caz de egalitate se compara in functie de start
in metoda run, aceasta ruleaza pana shutdownRequested devine true,se aplica sincronizarea pe taskQueue ,extrage un task din coada folosind poll(),
simuleaza executia task-ului,apeland functia simulateTaskExecution,finalizeaza task-ul cu finish() si se decrementeaza tasks.
in cazul in care coada e goala,thread-ul intra in wait.
sincronizarea pe taskQueue asigura ca accesul la coada se face atomic pentru a evita race conditions.
