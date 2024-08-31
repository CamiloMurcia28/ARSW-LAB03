package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostThreads extends Thread {

    private static final int BLACK_LIST_ALARM_COUNT = 5;
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    private int a;
    private int b;
    private String ipaddress;
    private List<Integer> blackListOcurrences;
    private static int ocurrencesCount = 0; // Contador compartido
    private static boolean stopSearch = false; // Señal para detener la búsqueda
    private static final Object lock = new Object(); // Bloque de sincronización

    public HostThreads(int a, int b, String ipaddress, List<Integer> blackListOcurrences) {
        this.a = a;
        this.b = b;
        this.ipaddress = ipaddress;
        this.blackListOcurrences = blackListOcurrences; // Compartir la misma instancia
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        int checkedListsCount = 0;

        for (int i = a; i < b; i++) {
        

            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                synchronized (lock) {
                    blackListOcurrences.add(i);
                    ocurrencesCount++;
                    System.out.println("Found occurrence: " + ocurrencesCount);
                    if (ocurrencesCount >= BLACK_LIST_ALARM_COUNT) {
                        break;
                    }
                }
            }
        }

        LOG.log(Level.INFO, "Checked Black Lists: {0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
    }

    public List<Integer> getList(){
        return blackListOcurrences;
    }
}


