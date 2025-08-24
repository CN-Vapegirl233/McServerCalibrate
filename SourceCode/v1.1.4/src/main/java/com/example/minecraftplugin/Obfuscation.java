package com.example.minecraftplugin;

public class Obfuscation {
    // 混淆方法 - 执行无意义但复杂的计算
    public static int obfuscate(int input) {
        int x = input;
        x = x ^ 0x5A5A5A5A;
        x = (x << 13) | (x >>> 19);
        x = x + 0x7E7E7E7E;
        x = x ^ 0x3C3C3C3C;
        x = (x << 17) | (x >>> 15);
        x = x + 0x1F1F1F1F;
        x = x ^ 0x80808080;
        return x;
    }

    // 反向混淆方法
    public static int deobfuscate(int input) {
        int x = input;
        x = x ^ 0x80808080;
        x = x - 0x1F1F1F1F;
        x = (x >>> 17) | (x << 15);
        x = x ^ 0x3C3C3C3C;
        x = x - 0x7E7E7E7E;
        x = (x >>> 13) | (x << 19);
        x = x ^ 0x5A5A5A5A;
        return x;
    }

    // 生成随机密钥
    public static String generateKey(int length) {
        StringBuilder key = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            key.append(chars.charAt(index));
        }
        return key.toString();
    }

    // 混淆字符串
    public static String obfuscateString(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append((char) (c ^ 0x5F));
        }
        return result.toString();
    }

    // 解混淆字符串
    public static String deobfuscateString(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append((char) (c ^ 0x5F));
        }
        return result.toString();
    }

    // 无用但复杂的方法，用于混淆代码
    public static void complexCalculation() {
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i * i % 17;
        }

        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }

        int sum = 0;
        for (int value : array) {
            sum += value;
        }

        // 结果不会被使用
        @SuppressWarnings("unused")
        double average = (double) sum / array.length;
    }
}