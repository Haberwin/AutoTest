/*     */ package com.mediatek.LoNg;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class FileOpHelper
/*     */ {
/*     */   /* Error */
/*     */   public static void doZip(String zipFileName, String dir)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 16	com/mediatek/LoNg/utils/log/Log:logger	Lorg/apache/log4j/Logger;
/*     */     //   3: new 22	java/lang/StringBuilder
/*     */     //   6: dup
/*     */     //   7: ldc 24
/*     */     //   9: invokespecial 26	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   16: ldc 33
/*     */     //   18: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   21: aload_0
/*     */     //   22: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   25: invokevirtual 35	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   28: invokevirtual 39	org/apache/log4j/Logger:info	(Ljava/lang/Object;)V
/*     */     //   31: aload_0
/*     */     //   32: ldc 45
/*     */     //   34: ldc 47
/*     */     //   36: invokevirtual 49	java/lang/String:replaceAll	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   39: astore_0
/*     */     //   40: aload_1
/*     */     //   41: ldc 45
/*     */     //   43: ldc 47
/*     */     //   45: invokevirtual 49	java/lang/String:replaceAll	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   48: astore_1
/*     */     //   49: new 55	java/io/File
/*     */     //   52: dup
/*     */     //   53: aload_1
/*     */     //   54: invokespecial 57	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   57: astore_2
/*     */     //   58: new 55	java/io/File
/*     */     //   61: dup
/*     */     //   62: aload_0
/*     */     //   63: invokespecial 57	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   66: astore_3
/*     */     //   67: aload_2
/*     */     //   68: invokevirtual 58	java/io/File:exists	()Z
/*     */     //   71: ifeq +267 -> 338
/*     */     //   74: aconst_null
/*     */     //   75: astore 4
/*     */     //   77: aconst_null
/*     */     //   78: astore 5
/*     */     //   80: new 62	java/io/FileOutputStream
/*     */     //   83: dup
/*     */     //   84: aload_3
/*     */     //   85: invokespecial 64	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   88: astore 4
/*     */     //   90: new 67	java/util/zip/ZipOutputStream
/*     */     //   93: dup
/*     */     //   94: new 69	java/io/BufferedOutputStream
/*     */     //   97: dup
/*     */     //   98: aload 4
/*     */     //   100: invokespecial 71	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   103: invokespecial 74	java/util/zip/ZipOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   106: astore 5
/*     */     //   108: aload_2
/*     */     //   109: invokevirtual 75	java/io/File:listFiles	()[Ljava/io/File;
/*     */     //   112: dup
/*     */     //   113: astore 9
/*     */     //   115: arraylength
/*     */     //   116: istore 8
/*     */     //   118: iconst_0
/*     */     //   119: istore 7
/*     */     //   121: goto +22 -> 143
/*     */     //   124: aload 9
/*     */     //   126: iload 7
/*     */     //   128: aaload
/*     */     //   129: astore 6
/*     */     //   131: ldc 79
/*     */     //   133: aload 6
/*     */     //   135: aload 5
/*     */     //   137: invokestatic 81	com/mediatek/LoNg/FileOpHelper:addEntry	(Ljava/lang/String;Ljava/io/File;Ljava/util/zip/ZipOutputStream;)V
/*     */     //   140: iinc 7 1
/*     */     //   143: iload 7
/*     */     //   145: iload 8
/*     */     //   147: if_icmplt -23 -> 124
/*     */     //   150: goto +148 -> 298
/*     */     //   153: astore 6
/*     */     //   155: aload 6
/*     */     //   157: invokevirtual 85	java/io/FileNotFoundException:printStackTrace	()V
/*     */     //   160: aload 5
/*     */     //   162: ifnull +18 -> 180
/*     */     //   165: aload 5
/*     */     //   167: invokevirtual 90	java/util/zip/ZipOutputStream:close	()V
/*     */     //   170: goto +10 -> 180
/*     */     //   173: astore 11
/*     */     //   175: aload 11
/*     */     //   177: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   180: aload 4
/*     */     //   182: ifnull +156 -> 338
/*     */     //   185: aload 4
/*     */     //   187: invokevirtual 96	java/io/FileOutputStream:close	()V
/*     */     //   190: goto +148 -> 338
/*     */     //   193: astore 11
/*     */     //   195: aload 11
/*     */     //   197: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   200: goto +138 -> 338
/*     */     //   203: astore 6
/*     */     //   205: aload 6
/*     */     //   207: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   210: aload 5
/*     */     //   212: ifnull +18 -> 230
/*     */     //   215: aload 5
/*     */     //   217: invokevirtual 90	java/util/zip/ZipOutputStream:close	()V
/*     */     //   220: goto +10 -> 230
/*     */     //   223: astore 11
/*     */     //   225: aload 11
/*     */     //   227: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   230: aload 4
/*     */     //   232: ifnull +106 -> 338
/*     */     //   235: aload 4
/*     */     //   237: invokevirtual 96	java/io/FileOutputStream:close	()V
/*     */     //   240: goto +98 -> 338
/*     */     //   243: astore 11
/*     */     //   245: aload 11
/*     */     //   247: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   250: goto +88 -> 338
/*     */     //   253: astore 10
/*     */     //   255: aload 5
/*     */     //   257: ifnull +18 -> 275
/*     */     //   260: aload 5
/*     */     //   262: invokevirtual 90	java/util/zip/ZipOutputStream:close	()V
/*     */     //   265: goto +10 -> 275
/*     */     //   268: astore 11
/*     */     //   270: aload 11
/*     */     //   272: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   275: aload 4
/*     */     //   277: ifnull +18 -> 295
/*     */     //   280: aload 4
/*     */     //   282: invokevirtual 96	java/io/FileOutputStream:close	()V
/*     */     //   285: goto +10 -> 295
/*     */     //   288: astore 11
/*     */     //   290: aload 11
/*     */     //   292: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   295: aload 10
/*     */     //   297: athrow
/*     */     //   298: aload 5
/*     */     //   300: ifnull +18 -> 318
/*     */     //   303: aload 5
/*     */     //   305: invokevirtual 90	java/util/zip/ZipOutputStream:close	()V
/*     */     //   308: goto +10 -> 318
/*     */     //   311: astore 11
/*     */     //   313: aload 11
/*     */     //   315: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   318: aload 4
/*     */     //   320: ifnull +18 -> 338
/*     */     //   323: aload 4
/*     */     //   325: invokevirtual 96	java/io/FileOutputStream:close	()V
/*     */     //   328: goto +10 -> 338
/*     */     //   331: astore 11
/*     */     //   333: aload 11
/*     */     //   335: invokevirtual 93	java/io/IOException:printStackTrace	()V
/*     */     //   338: return
/*     */     // Line number table:
/*     */     //   Java source line #17	-> byte code offset #0
/*     */     //   Java source line #18	-> byte code offset #31
/*     */     //   Java source line #19	-> byte code offset #40
/*     */     //   Java source line #21	-> byte code offset #49
/*     */     //   Java source line #22	-> byte code offset #58
/*     */     //   Java source line #24	-> byte code offset #67
/*     */     //   Java source line #25	-> byte code offset #74
/*     */     //   Java source line #26	-> byte code offset #77
/*     */     //   Java source line #28	-> byte code offset #80
/*     */     //   Java source line #29	-> byte code offset #90
/*     */     //   Java source line #35	-> byte code offset #108
/*     */     //   Java source line #36	-> byte code offset #131
/*     */     //   Java source line #35	-> byte code offset #140
/*     */     //   Java source line #38	-> byte code offset #150
/*     */     //   Java source line #40	-> byte code offset #155
/*     */     //   Java source line #45	-> byte code offset #160
/*     */     //   Java source line #47	-> byte code offset #165
/*     */     //   Java source line #48	-> byte code offset #170
/*     */     //   Java source line #50	-> byte code offset #175
/*     */     //   Java source line #52	-> byte code offset #180
/*     */     //   Java source line #54	-> byte code offset #185
/*     */     //   Java source line #55	-> byte code offset #190
/*     */     //   Java source line #57	-> byte code offset #195
/*     */     //   Java source line #41	-> byte code offset #203
/*     */     //   Java source line #43	-> byte code offset #205
/*     */     //   Java source line #45	-> byte code offset #210
/*     */     //   Java source line #47	-> byte code offset #215
/*     */     //   Java source line #48	-> byte code offset #220
/*     */     //   Java source line #50	-> byte code offset #225
/*     */     //   Java source line #52	-> byte code offset #230
/*     */     //   Java source line #54	-> byte code offset #235
/*     */     //   Java source line #55	-> byte code offset #240
/*     */     //   Java source line #57	-> byte code offset #245
/*     */     //   Java source line #44	-> byte code offset #253
/*     */     //   Java source line #45	-> byte code offset #255
/*     */     //   Java source line #47	-> byte code offset #260
/*     */     //   Java source line #48	-> byte code offset #265
/*     */     //   Java source line #50	-> byte code offset #270
/*     */     //   Java source line #52	-> byte code offset #275
/*     */     //   Java source line #54	-> byte code offset #280
/*     */     //   Java source line #55	-> byte code offset #285
/*     */     //   Java source line #57	-> byte code offset #290
/*     */     //   Java source line #61	-> byte code offset #295
/*     */     //   Java source line #45	-> byte code offset #298
/*     */     //   Java source line #47	-> byte code offset #303
/*     */     //   Java source line #48	-> byte code offset #308
/*     */     //   Java source line #50	-> byte code offset #313
/*     */     //   Java source line #52	-> byte code offset #318
/*     */     //   Java source line #54	-> byte code offset #323
/*     */     //   Java source line #55	-> byte code offset #328
/*     */     //   Java source line #57	-> byte code offset #333
/*     */     //   Java source line #65	-> byte code offset #338
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	339	0	zipFileName	String
/*     */     //   0	339	1	dir	String
/*     */     //   57	52	2	source	File
/*     */     //   66	19	3	zipFile	File
/*     */     //   75	249	4	fos	java.io.FileOutputStream
/*     */     //   78	226	5	zos	java.util.zip.ZipOutputStream
/*     */     //   129	5	6	file	File
/*     */     //   153	3	6	e	java.io.FileNotFoundException
/*     */     //   203	3	6	e	java.io.IOException
/*     */     //   119	29	7	i	int
/*     */     //   116	32	8	j	int
/*     */     //   113	12	9	arrayOfFile	File[]
/*     */     //   253	43	10	localObject	Object
/*     */     //   173	3	11	e	java.io.IOException
/*     */     //   193	3	11	e	java.io.IOException
/*     */     //   223	3	11	e	java.io.IOException
/*     */     //   243	3	11	e	java.io.IOException
/*     */     //   268	3	11	e	java.io.IOException
/*     */     //   288	3	11	e	java.io.IOException
/*     */     //   311	3	11	e	java.io.IOException
/*     */     //   331	3	11	e	java.io.IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   80	150	153	java/io/FileNotFoundException
/*     */     //   165	170	173	java/io/IOException
/*     */     //   185	190	193	java/io/IOException
/*     */     //   80	150	203	java/io/IOException
/*     */     //   215	220	223	java/io/IOException
/*     */     //   235	240	243	java/io/IOException
/*     */     //   80	160	253	finally
/*     */     //   203	210	253	finally
/*     */     //   260	265	268	java/io/IOException
/*     */     //   280	285	288	java/io/IOException
/*     */     //   303	308	311	java/io/IOException
/*     */     //   323	328	331	java/io/IOException
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   static void addEntry(String base, File source, java.util.zip.ZipOutputStream zos)
/*     */     throws java.io.IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 22	java/lang/StringBuilder
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokestatic 117	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   8: invokespecial 26	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 121	java/io/File:getName	()Ljava/lang/String;
/*     */     //   15: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   18: invokevirtual 35	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   21: astore_3
/*     */     //   22: aload_1
/*     */     //   23: invokevirtual 124	java/io/File:isDirectory	()Z
/*     */     //   26: ifeq +64 -> 90
/*     */     //   29: aload_1
/*     */     //   30: invokevirtual 75	java/io/File:listFiles	()[Ljava/io/File;
/*     */     //   33: dup
/*     */     //   34: astore 7
/*     */     //   36: arraylength
/*     */     //   37: istore 6
/*     */     //   39: iconst_0
/*     */     //   40: istore 5
/*     */     //   42: goto +38 -> 80
/*     */     //   45: aload 7
/*     */     //   47: iload 5
/*     */     //   49: aaload
/*     */     //   50: astore 4
/*     */     //   52: new 22	java/lang/StringBuilder
/*     */     //   55: dup
/*     */     //   56: aload_3
/*     */     //   57: invokestatic 117	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   60: invokespecial 26	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   63: ldc 47
/*     */     //   65: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   68: invokevirtual 35	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   71: aload 4
/*     */     //   73: aload_2
/*     */     //   74: invokestatic 81	com/mediatek/LoNg/FileOpHelper:addEntry	(Ljava/lang/String;Ljava/io/File;Ljava/util/zip/ZipOutputStream;)V
/*     */     //   77: iinc 5 1
/*     */     //   80: iload 5
/*     */     //   82: iload 6
/*     */     //   84: if_icmplt -39 -> 45
/*     */     //   87: goto +137 -> 224
/*     */     //   90: aconst_null
/*     */     //   91: astore 4
/*     */     //   93: aconst_null
/*     */     //   94: astore 5
/*     */     //   96: sipush 1024
/*     */     //   99: newarray <illegal type>
/*     */     //   101: astore 6
/*     */     //   103: new 127	java/io/FileInputStream
/*     */     //   106: dup
/*     */     //   107: aload_1
/*     */     //   108: invokespecial 129	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   111: astore 4
/*     */     //   113: new 130	java/io/BufferedInputStream
/*     */     //   116: dup
/*     */     //   117: aload 4
/*     */     //   119: aload 6
/*     */     //   121: arraylength
/*     */     //   122: invokespecial 132	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
/*     */     //   125: astore 5
/*     */     //   127: iconst_0
/*     */     //   128: istore 7
/*     */     //   130: aload_2
/*     */     //   131: new 135	java/util/zip/ZipEntry
/*     */     //   134: dup
/*     */     //   135: aload_3
/*     */     //   136: invokespecial 137	java/util/zip/ZipEntry:<init>	(Ljava/lang/String;)V
/*     */     //   139: invokevirtual 138	java/util/zip/ZipOutputStream:putNextEntry	(Ljava/util/zip/ZipEntry;)V
/*     */     //   142: goto +12 -> 154
/*     */     //   145: aload_2
/*     */     //   146: aload 6
/*     */     //   148: iconst_0
/*     */     //   149: iload 7
/*     */     //   151: invokevirtual 142	java/util/zip/ZipOutputStream:write	([BII)V
/*     */     //   154: aload 5
/*     */     //   156: aload 6
/*     */     //   158: iconst_0
/*     */     //   159: aload 6
/*     */     //   161: arraylength
/*     */     //   162: invokevirtual 146	java/io/BufferedInputStream:read	([BII)I
/*     */     //   165: dup
/*     */     //   166: istore 7
/*     */     //   168: iconst_m1
/*     */     //   169: if_icmpne -24 -> 145
/*     */     //   172: aload_2
/*     */     //   173: invokevirtual 150	java/util/zip/ZipOutputStream:closeEntry	()V
/*     */     //   176: goto +28 -> 204
/*     */     //   179: astore 8
/*     */     //   181: aload 5
/*     */     //   183: ifnull +8 -> 191
/*     */     //   186: aload 5
/*     */     //   188: invokevirtual 153	java/io/BufferedInputStream:close	()V
/*     */     //   191: aload 4
/*     */     //   193: ifnull +8 -> 201
/*     */     //   196: aload 4
/*     */     //   198: invokevirtual 154	java/io/FileInputStream:close	()V
/*     */     //   201: aload 8
/*     */     //   203: athrow
/*     */     //   204: aload 5
/*     */     //   206: ifnull +8 -> 214
/*     */     //   209: aload 5
/*     */     //   211: invokevirtual 153	java/io/BufferedInputStream:close	()V
/*     */     //   214: aload 4
/*     */     //   216: ifnull +8 -> 224
/*     */     //   219: aload 4
/*     */     //   221: invokevirtual 154	java/io/FileInputStream:close	()V
/*     */     //   224: return
/*     */     // Line number table:
/*     */     //   Java source line #70	-> byte code offset #0
/*     */     //   Java source line #71	-> byte code offset #22
/*     */     //   Java source line #72	-> byte code offset #29
/*     */     //   Java source line #74	-> byte code offset #52
/*     */     //   Java source line #72	-> byte code offset #77
/*     */     //   Java source line #76	-> byte code offset #87
/*     */     //   Java source line #77	-> byte code offset #90
/*     */     //   Java source line #78	-> byte code offset #93
/*     */     //   Java source line #80	-> byte code offset #96
/*     */     //   Java source line #81	-> byte code offset #103
/*     */     //   Java source line #82	-> byte code offset #113
/*     */     //   Java source line #83	-> byte code offset #127
/*     */     //   Java source line #85	-> byte code offset #130
/*     */     //   Java source line #86	-> byte code offset #142
/*     */     //   Java source line #87	-> byte code offset #145
/*     */     //   Java source line #86	-> byte code offset #154
/*     */     //   Java source line #89	-> byte code offset #172
/*     */     //   Java source line #90	-> byte code offset #176
/*     */     //   Java source line #91	-> byte code offset #181
/*     */     //   Java source line #92	-> byte code offset #186
/*     */     //   Java source line #93	-> byte code offset #191
/*     */     //   Java source line #94	-> byte code offset #196
/*     */     //   Java source line #95	-> byte code offset #201
/*     */     //   Java source line #91	-> byte code offset #204
/*     */     //   Java source line #92	-> byte code offset #209
/*     */     //   Java source line #93	-> byte code offset #214
/*     */     //   Java source line #94	-> byte code offset #219
/*     */     //   Java source line #97	-> byte code offset #224
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	225	0	base	String
/*     */     //   0	225	1	source	File
/*     */     //   0	225	2	zos	java.util.zip.ZipOutputStream
/*     */     //   21	115	3	entry	String
/*     */     //   50	22	4	file	File
/*     */     //   91	129	4	fis	java.io.FileInputStream
/*     */     //   40	45	5	i	int
/*     */     //   94	116	5	bis	java.io.BufferedInputStream
/*     */     //   37	48	6	j	int
/*     */     //   101	59	6	buffer	byte[]
/*     */     //   34	12	7	arrayOfFile	File[]
/*     */     //   128	39	7	readCount	int
/*     */     //   179	23	8	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   96	179	179	finally
/*     */   }
/*     */   
/*     */   public static boolean deleteDirectory(File directory)
/*     */   {
/* 100 */     if (directory.exists()) {
/* 101 */       File[] files = directory.listFiles();
/* 102 */       if (files != null) {
/* 103 */         for (int i = 0; i < files.length; i++) {
/* 104 */           if (files[i].isDirectory()) {
/* 105 */             deleteDirectory(files[i]);
/*     */           } else {
/* 107 */             files[i].delete();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 112 */     return directory.delete();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 116 */     String zipPath = "D:/temp/ALPS01039623/20130929_101350_883_path_unix.zip";
/* 117 */     String pcPath = "D:/temp/ALPS01039623/20130929_101350_883/";
/* 118 */     doZip(zipPath, pcPath);
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/FileOpHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */