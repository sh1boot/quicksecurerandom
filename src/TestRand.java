public class TestRand {
    public class MySecureRandom extends java.security.SecureRandom {
        private long m = 1;
        private long r = 0;
        public long bits = 0;

        @Override
        public final int nextInt(int n) {
            while (true) {
                if (m < 0x80000000L) {
                    m <<= 32;
                    r <<= 32;
                    bits += 32;
                    r += (long)next(32) - Integer.MIN_VALUE;
                }
                long q = m / n;
                if (r < n * q) {
                    int x = (int)(r % n);
                    m = q;
                    r /= n;
                    return x;
                }
                m -= n * q;
                r -= n * q;
            }
        }
    }

    public class MyRandom extends java.util.Random {
        private long m = 1;
        private long r = 0;
        public long bits = 0;

        @Override
        public final int nextInt(int n) {
            while (true) {
                if (m < 0x80000000L) {
                    m <<= 32;
                    r <<= 32;
                    bits += 32;
                    r += (long)next(32) - Integer.MIN_VALUE;
                }
                long q = m / n;
                if (r < n * q) {
                    int x = (int)(r % n);
                    m = q;
                    r /= n;
                    return x;
                }
                m -= n * q;
                r -= n * q;
            }
        }
    }

    private void test(java.util.Random r) {
        int N = 1000000;
        int MAX = 52;
        int[] hist = new int[256];
        int i, j;
        long time = 0;
        
        r.setSeed(0x123456);

        for (i = 2; i < MAX; i++) {
            int[] arr = new int[i];
            long t = System.currentTimeMillis();
            for (j = 0; j < N; j++) {
                int x = r.nextInt(i);
                arr[x]++;
            }
            time += System.currentTimeMillis() - t;

            for (j = 0; j < i; j++)
                hist[(j * hist.length) / i] += arr[j];
        }

        if (false)
        {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (i = 0; i < hist.length; i++) {
                max = Math.max(max, hist[i]);
                min = Math.min(min, hist[i]);
            }
            System.out.printf("min: %d, max: %d, %%: %.2f\n", min, max, 100.0 * min / max);
        }

        System.out.printf("%s took %dms\n", r.getClass().toString(), time);
    }

    private void test_next(java.util.Random r, long bits) {
        long i;
        long time, t;

        r.setSeed(0x123456);

        t = System.currentTimeMillis();
        for (i = 0; i < bits; i += 32) {
            r.nextInt();
        }
        time = System.currentTimeMillis() - t;
        System.out.printf("%s.nextInt() took %dms\n", r.getClass().toString(), time);
        t = System.currentTimeMillis();
        for (i = 0; i < bits; i += 16) {
            r.nextInt(65536);
        }
        time = System.currentTimeMillis() - t;
        System.out.printf("%s.nextInt(65536) took %dms\n", r.getClass().toString(), time);
        t = System.currentTimeMillis();
        for (i = 0; i < bits; i += 8) {
            r.nextInt(256);
        }
        time = System.currentTimeMillis() - t;
        System.out.printf("%s.nextInt(256) took %dms\n", r.getClass().toString(), time);
        t = System.currentTimeMillis();
        for (i = 0; i < bits; i += 4) {
            r.nextInt(16);
        }
        time = System.currentTimeMillis() - t;
        System.out.printf("%s.nextInt(16) took %dms\n", r.getClass().toString(), time);
    }

    public static void main(String[] args) {
        TestRand t = new TestRand();
        MyRandom mr = t.new MyRandom();
        java.util.Random jr = new java.util.Random();
        MySecureRandom msr = t.new MySecureRandom();
        java.security.SecureRandom jsr = new java.security.SecureRandom();

        t.test(mr);
        System.out.printf("bits used: %d\n\n", mr.bits);
        t.test(jr);
        t.test_next(jr, mr.bits);
        System.out.append('\n');
        t.test(msr);
        System.out.printf("bits used: %d\n\n", msr.bits);
        t.test(jsr);
        t.test_next(jsr, msr.bits);
        System.out.append('\n');
    }
}
