/**
 * Created by KnightC on 2015/7/10.
 */
package dtp.wechat

import grails.converters.JSON

class PathUtils {

    static createDir(temp){
        if(!temp.exists()){
            temp.mkdirs()
        }
    }
    static deleteFile(temp){
        if(temp.exists()){
            temp.delete()
        }
    }
    static getYearMonth(){
        Calendar c = new GregorianCalendar();
        c.add(Calendar.MONTH, +1);
        def month=c.get(Calendar.MONTH)
        def year=c.get(Calendar.YEAR)
        def year_month;
        if(c.get(Calendar.MONTH)<10){
            year_month=year+"0"+month
        }else{
            year_month=year+""+month
        }
        return year_month
    }

    static getDayAfter(date,afterDay){
        Calendar c = new GregorianCalendar();
        c.setTime(date)
        c.add(Calendar.MONTH, +1);
        c.add(Calendar.DAY_OF_MONTH, +afterDay);
        def year=c.get(Calendar.YEAR)
        def month=c.get(Calendar.MONTH)
        def day=c.get(Calendar.DAY_OF_MONTH)
        return JSON.parse("{year:${year},month:${month},day:${day}}")
    }

    static dateCompare(time){
        Calendar c = new GregorianCalendar();
        c.set(time.year,time.month,time.day)
        Calendar now = new GregorianCalendar();
        now.add(Calendar.MONTH, +1);
        return now.after(c)
    }



    static splitFilename(fileName) {
        def idx = fileName.lastIndexOf(".")
        def name = fileName
        def ext = ""
        if (idx > 0) {
            name = fileName[0..idx - 1]
            if(fileName.length() > idx + 1) {
                ext = fileName[idx + 1..-1]
            }
        }
        return [name: name, ext: ext]
    }

    static getFilePath(fileName) {
        def idx = fileName.lastIndexOf(File.separator)
        def path = fileName[0..idx]
        return path
    }

    static sanitizePath(path) {
        def result = ""
        if (path) {
            // remove: . \ / | : ? * " ' ` ~ < > {space}
            result = path.replaceAll(/\.|\/|\\|\||:|\?|\*|"|'|~|`|<|>| /, "")
        }
        return result
    }

    /**
     * Remove or add slashes as indicated in rules
     *
     * rules: space separated list of rules
     *      R- = remove slash on right
     *      R+ = add slash on right
     *      L- = remove slash on left
     *      L+ = add slash on left
     */
    static checkSlashes(path, rules, isUrl = false) {
        def result = path?.trim()
        if (result) {
            def rls = rules.split(' ')
            def separator = isUrl ? '/' : File.separator
            rls.each { r ->
                def isAdd = (r[1] == '+')

                if (isAdd) {
                    if (r[0].toUpperCase() == 'L') {
                        // Add separator on left
                        if (!result.startsWith('/') && !result.startsWith('\\')  ) {
                            result = separator + result
                        }
                    }
                    else {
                        // Add separator on right
                        if (!result.endsWith('/') && !result.endsWith('\\')  ) {
                            result = result + separator
                        }
                    }
                }
                else {
                    if (r[0].toUpperCase() == 'L') {
                        // Remove separator on left
                        if (result.startsWith('/') || result.startsWith('\\')  ) {
                            result = result.substring(1)
                        }
                    }
                    else {
                        // Remove separator on right
                        if (result.endsWith('/') || result.endsWith('\\')  ) {
                            result = result[0..-2]
                        }
                    }
                }
            }
        }
        return result
    }

    static normalizePath(path) {
        def el = path.tokenize(File.separator)
        def p = []
        for(e in el) {
            if (e == ".") {
                // skip
            }
            else if (e == "..") {
                p.pop()
            }
            else {
                p << e
            }
        }

        def result = "" << ""
        if (path.startsWith(File.separator)) {
            result << File.separator
        }

        result << p.join(File.separator)

        if (path.endsWith(File.separator)) {
            result << File.separator
        }

        return result.toString()
    }

    static isSafePath(baseDir, file) {
        def p = normalizePath(file.absolutePath)
        return p.startsWith(baseDir)
    }


    static  isheader(url){
        int index=url.indexOf("http://");
        if(index==-1){
            url="http://"+url
        }
        return url
    }
}
