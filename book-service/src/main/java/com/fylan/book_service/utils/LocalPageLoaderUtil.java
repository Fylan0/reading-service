package com.fylan.book_service.utils;

import com.fylan.book_service.domain.po.BookChapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalPageLoaderUtil {
    //默认从文件中获取数据的长度
    private final static int BUFFER_SIZE = 512 * 1024;
    //没有标题的时候，每个章节的最大长度
    private final static int MAX_LENGTH_WITH_NO_CHAPTER = 10 * 1024;

    // "序(章)|前言"
    private final static Pattern mPreChapterPattern = Pattern.compile("^(\\s{0,10})((\u5e8f[\u7ae0\u8a00]?)|(\u524d\u8a00)|(\u6954\u5b50))(\\s{0,10})$", Pattern.MULTILINE);

    //正则表达式章节匹配模式
    // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
    private static final String[] CHAPTER_PATTERNS = new String[]{"^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\f\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"};

    //章节解析模式
    private static Pattern mChapterPattern = null;


    public static List<BookChapter> loadChapters(
            File bookFile, String bookId) throws IOException {
        List<BookChapter> chapters = new ArrayList<>();
        //获取文件流
        RandomAccessFile bookStream = new RandomAccessFile(bookFile, "r");

        //寻找匹配文章标题的正则表达式，判断是否存在章节名
        boolean hasChapter = checkChapterType(bookStream);


        byte[] buffer = new byte[BUFFER_SIZE];
        int readLength;
        int curOffset = 0;

        //block的个数
        int blockPos = 0;


        try {
            while ((readLength = bookStream.read(buffer, 0, buffer.length)) > 0) {

                if (hasChapter) {


                    String blockContent = new String(buffer, 0, readLength);

                    int seekPos = 0;
                    Matcher matcher = mChapterPattern.matcher(blockContent);

                    while (matcher.find()) {
                        int chapterStart = matcher.start();
                        if (chapterStart > 0) {
                            String content = blockContent.substring(seekPos, chapterStart);
                            seekPos += content.length();

                            if (chapters.isEmpty()) {

                                BookChapter prologue = new BookChapter();
                                prologue.setBookId(bookId);
                                prologue.setChapterName("序章");
                                prologue.setChapterContent(content);
                                prologue.setChapterNumber(0);
                                chapters.add(prologue);
                            } else {
                                BookChapter lastChapter = chapters.get(chapters.size() - 1);
                                lastChapter.setChapterContent(lastChapter.getChapterContent() + content);
                            }

                            BookChapter newChapter = new BookChapter();
                            newChapter.setBookId(bookId);
                            newChapter.setChapterName(matcher.group());
                            newChapter.setChapterNumber(chapters.size());
                            chapters.add(newChapter);
                        } else {
                            String content = blockContent.substring(seekPos, matcher.start());
                            if (chapters.isEmpty()) {
                                BookChapter lastChapter = chapters.get(chapters.size() - 1);
                                lastChapter.setChapterContent(lastChapter.getChapterContent() + content);
                            } else {
                                BookChapter newChapter = new BookChapter();
                                newChapter.setBookId(bookId);
                                newChapter.setChapterName(matcher.group());
                                newChapter.setChapterNumber(chapters.size());
                                newChapter.setChapterContent(content);
                                chapters.add(newChapter);
                            }
                        }
                    }
                }

                //进行本地虚拟分章
                else {
                    //章节在buffer的偏移量
                    int chapterOffset = 0;
                    //当前剩余可分配的长度
                    int strLength = readLength;
                    //分章的位置
                    int chapterPos = 0;

                    while (strLength > 0) {
                        ++chapterPos;
                        //是否长度超过一章
                        if (strLength > MAX_LENGTH_WITH_NO_CHAPTER) {
                            //在buffer中一章的终止点
                            int end = readLength;
                            //寻找换行符作为终止点
                            for (int i = chapterOffset + MAX_LENGTH_WITH_NO_CHAPTER; i < readLength; ++i) {
                                if (buffer[i] == Charset.BLANK) {
                                    end = i;
                                    break;
                                }
                            }
                            BookChapter chapter = new BookChapter();
                            chapter.setBookId(bookId);
                            chapter.setChapterName("第" + blockPos + "章" + "(" + chapterPos + ")");
//                        chapter.start = curOffset + chapterOffset + 1;
//                        chapter.end = curOffset + end;
                            chapters.add(chapter);
                            //减去已经被分配的长度
                            strLength = strLength - (end - chapterOffset);
                            //设置偏移的位置
                            chapterOffset = end;
                        } else {
                            BookChapter chapter = new BookChapter();
                            chapter.setBookId(bookId);
                            chapter.setChapterName("第" + blockPos + "章" + "(" + chapterPos + ")");
//                        chapter.start = curOffset + chapterOffset + 1;
//                        chapter.end = curOffset + length;
                            chapters.add(chapter);
                            strLength = 0;
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chapters;
    }

    /**
     * 未完成的部分:
     * 1. 序章的添加
     * 2. 章节存在的书本的虚拟分章效果
     *
     * @throws IOException
     */
    public static List<BookChapter> loadChapters2(File bookFile, String bookId) throws IOException {
        List<BookChapter> chapters = new ArrayList<>();
        //获取文件流
        RandomAccessFile bookStream = new RandomAccessFile(bookFile, "r");
        //寻找匹配文章标题的正则表达式，判断是否存在章节名
        boolean hasChapter = checkChapterType(bookStream);
        //加载章节
        byte[] buffer = new byte[BUFFER_SIZE];
        //获取到的块起始点，在文件中的位置
        long curOffset = 0;
        //block的个数
        int blockPos = 0;
        //读取的长度
        int length;

        //获取文件中的数据到buffer，直到没有数据为止
        while ((length = bookStream.read(buffer, 0, buffer.length)) > 0) {
            ++blockPos;
            //如果存在Chapter
            if (hasChapter) {
                //将数据转换成String
                String blockContent = new String(buffer, 0, length, Charset.UTF8.getName());
                //当前Block下使过的String的指针
                int seekPos = 0;
                //进行正则匹配
                Matcher matcher = mChapterPattern.matcher(blockContent);
                //如果存在相应章节
                while (matcher.find()) {
                    //获取匹配到的字符在字符串中的起始位置
                    int chapterStart = matcher.start();

                    //如果 seekPos == 0 && nextChapterPos != 0 表示当前block处前面有一段内容
                    //第一种情况一定是序章 第二种情况可能是上一个章节的内容
                    if (seekPos == 0 && chapterStart != 0) {
                        //获取当前章节的内容
                        String chapterContent = blockContent.substring(seekPos, chapterStart);
                        //设置指针偏移
                        seekPos += chapterContent.length();

                        //如果当前对整个文件的偏移位置为0的话，那么就是序章
                        if (curOffset == 0) {
                            //创建序章
                            BookChapter preChapter = new BookChapter();
                            preChapter.setBookId(bookId);
                            preChapter.setChapterName("序章");
                            preChapter.setChapterContent(chapterContent);
//                            preChapter.start = 0;
//                            preChapter.end = chapterContent.getBytes(mCharset.getName()).length; //获取String的byte值,作为最终值

                            //如果序章大小大于30才添加进去
//                            if (preChapter.end - preChapter.start > 30) {
                            preChapter.setChapterNumber(chapters.size());
                            chapters.add(preChapter);
//                            }

                            //创建当前章节
                            BookChapter curChapter = new BookChapter();
                            curChapter.setBookId(bookId);
                            curChapter.setChapterName(matcher.group());
//                            curChapter.start = preChapter.end;
                            preChapter.setChapterNumber(chapters.size());
                            chapters.add(curChapter);
                        }
                        //否则就block分割之后，上一个章节的剩余内容
                        else {
                            //获取上一章节
                            BookChapter lastChapter = chapters.get(chapters.size() - 1);
                            lastChapter.setChapterContent(lastChapter.getChapterContent() + chapterContent);
                            //将当前段落添加上一章去
//                            lastChapter.end += chapterContent.getBytes(mCharset.getName()).length;

                            //如果章节内容太小，则移除
//                            if (lastChapter.end - lastChapter.start < 30) {
//                            chapters.remove(lastChapter);
//                            }

                            //创建当前章节
                            BookChapter curChapter = new BookChapter();
                            curChapter.setBookId(bookId);
                            curChapter.setChapterName(matcher.group());
//                            curChapter.start = lastChapter.end;
                            curChapter.setChapterNumber(chapters.size());
                            chapters.add(curChapter);
                        }
                    } else {
                        //是否存在章节
                        if (chapters.size() != 0) {
                            //获取章节内容
                            String chapterContent = blockContent.substring(seekPos, matcher.start());
                            seekPos += chapterContent.length();

                            //获取上一章节
                            BookChapter lastChapter = chapters.get(chapters.size() - 1);
                            lastChapter.setChapterContent(lastChapter.getChapterContent() + chapterContent);

//                            lastChapter.end = lastChapter.start + chapterContent.getBytes(mCharset.getName()).length;

                            //如果章节内容太小，则移除
//                            if (lastChapter.end - lastChapter.start < 30) {
//                            chapters.remove(lastChapter);
//                            }

                            //创建当前章节
                            BookChapter curChapter = new BookChapter();
                            curChapter.setBookId(bookId);
                            curChapter.setChapterName(matcher.group());
//                            curChapter.start = lastChapter.end;
                            curChapter.setChapterNumber(chapters.size());
                            chapters.add(curChapter);
                        }
                        //如果章节不存在则创建章节
                        else {
                            BookChapter curChapter = new BookChapter();
                            curChapter.setBookId(bookId);
                            curChapter.setChapterName(matcher.group());
//                            curChapter.start = 0;
                            curChapter.setChapterNumber(chapters.size());
                            chapters.add(curChapter);
                        }
                    }
                }
            }
            //进行本地虚拟分章
            else {
                //章节在buffer的偏移量
                int chapterOffset = 0;
                //当前剩余可分配的长度
                int strLength = length;
                //分章的位置
                int chapterPos = 0;

                while (strLength > 0) {
                    ++chapterPos;
                    //是否长度超过一章
                    if (strLength > MAX_LENGTH_WITH_NO_CHAPTER) {
                        //在buffer中一章的终止点
                        int end = length;
                        //寻找换行符作为终止点
                        for (int i = chapterOffset + MAX_LENGTH_WITH_NO_CHAPTER; i < length; ++i) {
                            if (buffer[i] == Charset.BLANK) {
                                end = i;
                                break;
                            }
                        }
                        BookChapter chapter = new BookChapter();
                        chapter.setBookId(bookId);
                        chapter.setChapterName("第" + blockPos + "章" + "(" + chapterPos + ")");
//                        chapter.start = curOffset + chapterOffset + 1;
//                        chapter.end = curOffset + end;
                        chapters.add(chapter);
                        //减去已经被分配的长度
                        strLength = strLength - (end - chapterOffset);
                        //设置偏移的位置
                        chapterOffset = end;
                    } else {
                        BookChapter chapter = new BookChapter();
                        chapter.setBookId(bookId);
                        chapter.setChapterName("第" + blockPos + "章" + "(" + chapterPos + ")");
//                        chapter.start = curOffset + chapterOffset + 1;
//                        chapter.end = curOffset + length;
                        chapters.add(chapter);
                        strLength = 0;
                    }
                }
            }

            //block的偏移点
            curOffset += length;

            if (hasChapter) {
                //设置上一章的结尾
                BookChapter lastChapter = chapters.get(chapters.size() - 1);
//                lastChapter.end = curOffset;
            }

            //当添加的block太多的时候，执行GC
            if (blockPos % 15 == 0) {
                System.gc();
//                System.runFinalization();
            }
        }


        System.gc();

        return chapters;
//        System.runFinalization();
    }


    /**
     * 1. 检查文件中是否存在章节名
     * 2. 判断文件中使用的章节名类型的正则表达式
     *
     * @return 是否存在章节名
     */
    private static boolean checkChapterType(RandomAccessFile bookStream) throws IOException {
        //首先获取128k的数据
        byte[] buffer = new byte[BUFFER_SIZE / 4];
        int length = bookStream.read(buffer, 0, buffer.length);
        //进行章节匹配
        for (String str : CHAPTER_PATTERNS) {
            Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(new String(buffer, 0, length, Charset.UTF8.getName()));
            //如果匹配存在，那么就表示当前章节使用这种匹配方式
            if (matcher.find()) {
                mChapterPattern = pattern;
                //重置指针位置
                bookStream.seek(0);
                return true;
            }
        }

        //重置指针位置
        bookStream.seek(0);
        return false;
    }

}
