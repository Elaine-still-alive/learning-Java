package com.company;

import java.net.*;

 //Создает несколько потоков для обработки URL-адресов, основанных на указанном URL-адресе


public class Crawler {
    private URLPool pool;

    public int numThreads = 4; // Количество работающих потоков


     //Корневой URL-адрес должен включать протокол (упрощает проверку посещенных URL-адресов)
    public Crawler(String root, int max) throws MalformedURLException {
        pool = new URLPool(max);

        URL rootURL = new URL(root);
        pool.add(new URLDepthPair(rootURL, 0));
    }

      //Создает потоки CrawlerTask для обработки URL-адресов

    public void crawl() {
        for (int i = 0; i < numThreads; i++) {
            CrawlerTask crawler = new CrawlerTask(pool);
            Thread thread = new Thread(crawler);
            thread.start();
        }
        // Если все потоки свободны, значит процесс поиска завершен
        while (pool.getWaitCount() != numThreads) {
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Отработано исключение InterruptedException - " +
                        e.getMessage());
            }
        }

        pool.printURLs();
    }
    //Создает сканер, который просматривает ссылки, начиная с корневого URL

    public static void main(String[] args) {

        if (args.length < 2 || args.length > 5) {
            System.err.println("Используйте для запуска: java Crawler <URL> <глубина> " +
                    "<время ожидания> -t <кол-во потоков>");
            System.exit(1);
        }

        // Вызов сканера
        try {
            Crawler crawler = new Crawler(args[0], Integer.parseInt(args[1]));

            switch (args.length) {
                case 3:
                    CrawlerTask.maxPatience = Integer.parseInt(args[2]);
                    break;
                case 4:
                    crawler.numThreads = Integer.parseInt(args[3]);
                    break;
                case 5:
                    CrawlerTask.maxPatience = Integer.parseInt(args[2]);
                    crawler.numThreads = Integer.parseInt(args[4]);
                    break;
            }
            crawler.crawl();
        }
        catch (MalformedURLException e) {
            System.err.println("Ошибка: URL " + args[0] + " не корректен");
            System.exit(1);
        }
        System.exit(0);
    }
}
