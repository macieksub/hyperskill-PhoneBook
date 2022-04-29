package phonebook;

import java.io.File;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        final String dirPath = "C:\\Users\\macie\\Downloads\\directory.txt";
        final String finPath = "C:\\Users\\macie\\Downloads\\find.txt";
//        final String dirPath = "C:\\Users\\macie\\Downloads\\small_directory.txt";
//        final String finPath = "C:\\Users\\macie\\Downloads\\small_find.txt";

        final String[] directory = fileToArr(dirPath);
        final String[] find = fileToArr(finPath);
        final int amountToFind = find.length;

//        System.out.println(directory.length);
        long breakTime = linearSearch(directory, find, amountToFind, false);
//        System.out.println();
        bubbleSortJumpSearch(directory, find, amountToFind, breakTime);
        quickSortBinarySearch(directory, find, amountToFind);
        hashTable(directory, find, amountToFind);
//        String[] aa = {"123 kk", "123 aa", "123 ff", "123 ee", "123 bb", "123 ii", "123 dd", "123 hh", "123 cc", "123 oo", "123 ll", "123 jj", "123 gg"};
//        String[] aa = {"123 ll", "123 kk", "123 cc"};
//        for (String a : aa) {
//            System.out.println(a);
//        }
//        quickSortBinarySearch(aa, find, amountToFind);
//        for (String a : directory) {
//            System.out.println(a);
//        }
    }


    static String[] fileToArr(String filePath) {
        File fileFin = new File(filePath);
        try (Scanner sc = new Scanner(fileFin)) {
            ArrayList<String> list = new ArrayList<>();
            while (sc.hasNext()) {
                list.add(sc.nextLine());
            }
            return list.toArray(new String[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String[] {"Empty"};
    }

    static long linearSearch(String[] directory, String[] find, int amountToFind, boolean bubble) {
        int amountFounded = 0;
        System.out.println("Start searching (linear search)...");
        final long start = System.nanoTime();

        for (String f : find) {
            for (String d : directory) {
                if (d.contains(f)) {
                    amountFounded++;
                    break;
                }
            }
        }

        final long end = System.nanoTime();

        if (bubble) {
            System.out.printf("Searching time: %d min. %d sec. %d ms.%n", ((end - start) / 60000000000L), ((end - start) / 1000000000) % 60, ((end - start) / 1000000) % 1000);
        } else {
            System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.%n", amountFounded, amountToFind, ((end - start) / 60000000000L), ((end - start) / 1000000000) % 60, ((end - start) / 1000000) % 1000);
        }
        return end - start;
    }

    static void bubbleSortJumpSearch(String[] directory, String[] find, int amountToFind, long breakTime) {
        System.out.println("Start searching (bubble sort + jump search)...");
        final long start = System.nanoTime();
        String pom;
        int length = directory.length - 1;
        for (int i = 0; i < length; length--) {
            if (System.nanoTime() - start > breakTime * 10) {
                System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", 500, amountToFind, ((System.nanoTime() - start + breakTime) / 60000000000L), ((System.nanoTime() - start + breakTime) / 1000000000) % 60, ((System.nanoTime() - start + breakTime) / 1000000) % 1000);
                System.out.printf("%nSorting time: %d min. %d sec. %d ms.%n", ((System.nanoTime() - start) / 60000000000L), ((System.nanoTime() - start) / 1000000000) % 60, ((System.nanoTime() - start) / 1000000) % 1000);
                System.out.println("STOPPED, moved to linear search");
                linearSearch(directory, find, amountToFind, true);
                return;
            }
            for (int j = i; j < length; j++) {
                if (directory[j].replaceAll("\\d", "").compareTo(directory[j + 1].replaceAll("\\d", "")) > 0) {
                    //                System.out.println(directory[i] + "\n" + directory[i + 1]);
                    pom = directory[j];
                    directory[j] = directory[j + 1];
                    directory[j + 1] = pom;
                }
            }
//            System.out.println(length);
        }
        final long sortEnd = System.nanoTime();

        int amountFounded = 0;

//        int t = 1;
//        for (String f : directory) {
//            System.out.println(f + " " + t++);
//        }
//        System.out.println(jumpSearch(directory, "Marcy Aubin"));
//        String find1 = "Ccc Nedra";
//        String directory1 = "56017076 Bbb Bugbee";
//        String directory2 = "50027149 Ddd Schlicher";
//        System.out.println(directory1.replaceAll("\\d", "").trim());
//        System.out.println(directory2.replaceAll("\\d", ""));
//        System.out.println(directory1.replaceAll("\\d", "").compareTo(find1) < 0);
//        System.out.println(directory2.replaceAll("\\d", "").compareTo(find1) < 0);

        for (String f : find) {
            amountFounded += jumpSearch(directory, f);
            //System.out.println(f);
        }
        final long end = System.nanoTime();
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", amountFounded, amountToFind, ((end - start) / 60000000000L), ((end - start) / 1000000000) % 60, ((end - start) / 1000000) % 1000);
        System.out.printf("%nSorting time: %d min. %d sec. %d ms.", ((sortEnd - start) / 60000000000L), ((sortEnd - start) / 1000000000) % 60, ((sortEnd - start) / 1000000) % 1000);
        System.out.printf("%nSearching time: %d min. %d sec. %d ms.%n%n", ((end - sortEnd) / 60000000000L), ((end - sortEnd) / 1000000000) % 60, ((end - sortEnd) / 1000000) % 1000);
    }

    static int jumpSearch (String[] directory, String find) {
        final int blockSize = (int) Math.sqrt(directory.length);
        for (int i = 0; i < directory.length; i += blockSize) {
            if (directory[i].contains(find)) {
                return 1;
            } else if (find.compareTo(directory[i].replaceAll("\\d", "").trim()) < 0) {
                for (int j = i - 1; j > i - blockSize; j--) {
                    if (directory[j].contains(find)) {
                        return 1;
                    }
                }
                return 0;
            }
        }
        for (int i = directory.length - 1; i > directory.length - directory.length % blockSize; i--) {
            if (directory[i].contains(find)) {
                return 1;
            }
        }
        //System.out.println();
        return 0;
    }

    static void quickSortBinarySearch(String[] directory, String[] find, int amountToFind) {
        System.out.println("Start searching (quick sort + binary search)...");
        int amountFounded = 0;
        final long start = System.nanoTime();
        quickSort(directory, 0, directory.length - 1);
        final long sortEnd = System.nanoTime();
        for (String a : find) {
            amountFounded += binarySearch(directory, a);
        }
//        System.out.println(binarySearch(directory, "Colette Ella"));
        final long end = System.nanoTime();
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", amountFounded, amountToFind, ((end - start) / 60000000000L), ((end - start) / 1000000000) % 60, ((end - start) / 1000000) % 1000);
        System.out.printf("%nSorting time: %d min. %d sec. %d ms.", ((sortEnd - start) / 60000000000L), ((sortEnd - start) / 1000000000) % 60, ((sortEnd - start) / 1000000) % 1000);
        System.out.printf("%nSearching time: %d min. %d sec. %d ms.", ((end - sortEnd) / 60000000000L), ((end - sortEnd) / 1000000000) % 60, ((end - sortEnd) / 1000000) % 1000);
    }

//    static int a = 0;
    static void quickSort(String[] directory, int start, int length) {
//        System.out.println(a++);
        int lengthCopy = length;
//        System.out.println("length - " + length);
//        System.out.println("Start - " + start);
        if (length <= start) {
            return;
        }
        final String pivot = directory[length];
//        System.out.println("pivot - " + pivot);
        String pom;
//        for (int i = start; i < length; i++) {
//        for (int i = start; i < length;) {
//            if (directory[i].replaceAll("\\d", "").compareTo(pivot.replaceAll("\\d", "")) > 0) {
//                //                System.out.println(directory[i] + "\n" + directory[i + 1]);
////                System.out.println(i + " " + length);
//                pom = directory[i];
//                for (int j = i; j < length; j++) {
//                    directory[j] = directory[j + 1];
//                }
//                directory[length] = pom;
//                length--;
////                for (String a : directory) {
////                    System.out.println(a);
////                }
////                System.out.println(length);
//            } else {
//                i++;
//            }
//        }
        for (int i = start; i < length; i++) {
            if (directory[i].replaceAll("\\d", "").compareTo(pivot.replaceAll("\\d", "")) > 0) {
                pom = directory[i];
                for (int j = length - 1; j >= i; j--) {
                    if (directory[j].replaceAll("\\d", "").compareTo(pivot.replaceAll("\\d", "")) < 0 || j == i) {
                        directory[i] = directory[j];
                        directory[j] = pivot;
                        directory[length] = pom;
                        length = j;
                        break;
                    }
//                    else if (j - 1 == i) {
//
//                    }
                }
            }
//            for (String e : directory) {
//                if (e.equals(pivot)) {
//                    System.out.println();
//                    System.out.println(e);
//                    System.out.println();
//                } else {
//                    System.out.println(e);
//                }
//            }
//            System.out.println();
//            System.out.println();
        }

//        if (directory[length - 1].replaceAll("\\d", "").compareTo(pivot.replaceAll("\\d", "")) > 0) {
//            pom = directory[length - 1];
//            directory[length - 1] = pivot;
//            directory[length] = pom;
//            length--;
//        }
//        for (String e : directory) {
//            System.out.println(e);
//        }
//        if (length == start) {
//            start++;
//        }
        quickSort(directory, start, length - 1);
        quickSort(directory, length + 1, lengthCopy);
    }
    static int binarySearch(String[] directory, String find) {
        int left = 0;
        int right = directory.length - 1;
        int middle;

        while (left <= right) {
            middle = (left + right) / 2;
//            System.out.println(directory[middle]);
            if (directory[middle].contains(find)) {
                return 1;
            } else if (directory[middle].replaceAll("\\d", "").trim().compareTo(find) > 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return 0;
    }

    static void hashTable(String[] directory, String[] find, int amountToFind) {
        System.out.println("Start searching (hash table)...");
        int amountFounded = 0;
        final long start = System.nanoTime();
//        int[][] hashDirectory = new int[directory.length][2];
        int[] hashDirectory = new int[directory.length];
        for (int i = 0; i < directory.length; i++) {
            hashDirectory[i] = directory[i].replaceAll("\\d", "").trim().hashCode();
//            hashDirectory[i][1] = Integer.parseInt(directory[i].replaceAll("\\D", ""));
        }
        final long sortEnd = System.nanoTime();
        int c;
        for (String a : find) {
            c = a.hashCode();
            for (int b : hashDirectory) {
                if (c == b) {
                    amountFounded++;
                    break;
                }
            }
        }
//        System.out.println(binarySearch(directory, "Colette Ella"));
        final long end = System.nanoTime();
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", amountFounded, amountToFind, ((end - start) / 60000000000L), ((end - start) / 1000000000) % 60, ((end - start) / 1000000) % 1000);
        System.out.printf("%nCreating time: %d min. %d sec. %d ms.", ((sortEnd - start) / 60000000000L), ((sortEnd - start) / 1000000000) % 60, ((sortEnd - start) / 1000000) % 1000);
        System.out.printf("%nSearching time: %d min. %d sec. %d ms.", ((end - sortEnd) / 60000000000L), ((end - sortEnd) / 1000000000) % 60, ((end - sortEnd) / 1000000) % 1000);
    }
}