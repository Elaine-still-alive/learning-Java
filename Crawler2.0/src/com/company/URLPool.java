package com.company;

import java.util.*;


 //Отслеживает URL-адрес, который необходимо обработать, а также URL-адрес, который уже видели
public class URLPool {
    private int maxDepth;

    //Текущее количество потоков в состоянии ожидания (wait())
    private int waitCount = 0;

    private LinkedList<URLDepthPair> pendingURLs;

    private LinkedList<URLDepthPair> processedURLs;

    private HashSet<String> seenURLs; // Посещенные URL


     //Создает URL пул с заданной глубиной

    public URLPool(int max) {
        pendingURLs = new LinkedList<URLDepthPair>();
        processedURLs = new LinkedList<URLDepthPair>();
        seenURLs = new HashSet<String>();

        maxDepth = max;
    }

    public synchronized int getWaitCount() {
        return waitCount;
    }

    public synchronized void add(URLDepthPair nextPair) {
        String newURL = nextPair.getURL().toString();

        // Вырезает "/" из URL
        String trimURL = (newURL.endsWith("/")) ? newURL.substring(0, newURL.length() -1) : newURL;
        if (seenURLs.contains(trimURL)){
            return;
        }
        seenURLs.add(trimURL);

        if (nextPair.getDepth() < maxDepth) {
            pendingURLs.add(nextPair);
            notify(); // Сообщаем приостановленному потоку о новом URL
        }
        processedURLs.add(nextPair);
    }

    public synchronized URLDepthPair get() {
        // Приостанавливаем поток, пока не появится новый URL
        while (pendingURLs.size() == 0) {
            waitCount++;
            try {
                wait();
            }
            catch (InterruptedException e) {
                System.out.println("Отработано исключение InterruptedException - " +
                        e.getMessage());
            }
            waitCount--;
        }

        return pendingURLs.removeFirst();
    }


      //Выводим все обработанные URL

    public synchronized void printURLs() {
        System.out.println("\nКол-во уникальных URLs: " + processedURLs.size());
        while (!processedURLs.isEmpty()) {
            System.out.println(processedURLs.removeFirst());
        }
    }
}