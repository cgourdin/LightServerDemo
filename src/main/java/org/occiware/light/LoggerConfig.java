/** 
 * Copyright 2016 - Christophe Gourdin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.occiware.light;


import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.varia.LevelRangeFilter;

/**
 * Configurer loggers for initialization.
 *
 * @author christophe
 */
public class LoggerConfig {

    /**
     * Initialise les appenders sur le root logger. 
     * Attention, tous les anciens appenders sont éliminé en début de méthode.
     */
    public static void initAppenders() {
        // Reinitialise le rootlogger.
        Logger.getRootLogger().getLoggerRepository().resetConfiguration();
        
        // TODO : Preferences : Activer / désactiver le logging via les preferences (Application).
        
        ConsoleAppender console = new ConsoleAppender();

        RollingFileAppender rollingInfoAppender;
        RollingFileAppender rollingDebugAppender;
        RollingFileAppender rollingWarnAppender;
        RollingFileAppender rollingErrorAppender;
        RollingFileAppender rollingFatalAppender;

        final String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %m%n";
        console.setLayout(new PatternLayout(pattern));
        // A tous les niveaux pour les devs, on assigne la console appender.
        console.setThreshold(Level.INFO); // Debug pour la version developpement, passera à info pour version prod.
        console.activateOptions();
        console.setEncoding("UTF8");

        Logger.getRootLogger().addAppender(console);
        String name = "DebugLogger";
        String filename = "log/debug.log";
        rollingDebugAppender = createAppender(Level.DEBUG, pattern, name, filename);
        name = "InfoLogger";
        filename = "log/info.log";
        rollingInfoAppender = createAppender(Level.INFO, pattern, name, filename);
        name = "WarnLogger";
        filename = "log/warn.log";
        rollingWarnAppender = createAppender(Level.WARN, pattern, name, filename);

        name = "ErrorLogger";
        filename = "log/error.log";
        rollingErrorAppender = createAppender(Level.ERROR, pattern, name, filename);

        name = "FatalLogger";
        filename = "log/fatal.log";
        rollingFatalAppender = createAppender(Level.FATAL, pattern, name, filename);
        

        // Assigne les appenders au logger.
        Logger.getRootLogger().addAppender(rollingDebugAppender);
        Logger.getRootLogger().addAppender(rollingInfoAppender);
        Logger.getRootLogger().addAppender(rollingWarnAppender);
        Logger.getRootLogger().addAppender(rollingErrorAppender);
        Logger.getRootLogger().addAppender(rollingFatalAppender);
        
        // Positionne les niveaux de log par classe.
        // Pour quartz.
//        name = PackageFilter.QUARTZ_LOGGER_NAME;
//        filename = "log/jobs.log";
        
//        RollingFileAppender rollingQuartzAppender = createAppender(Level.INFO, pattern, name, filename);
//        Logger logQuartz = Logger.getLogger(PackageFilter.QUARTZ_LOGGER_NAME);
//        logQuartz.setLevel(Level.INFO);
//        logQuartz.removeAllAppenders();
//        logQuartz.addAppender(rollingQuartzAppender);
//        logQuartz.setAdditivity(false);
        
    }

    /**
     * 
     * @param level 
     * @param pattern
     * @param name
     * @param filename
     * @return 
     */
    private static RollingFileAppender createAppender(final Level level, final String pattern, final String name, final String filename) {
        RollingFileAppender rollingAppender = new RollingFileAppender();
        String maxFileSize = "2048KB";
        int maxBackupIndex = 300;
        
        // LevelRangeFilter 
        LevelRangeFilter rangeFilter = new LevelRangeFilter();
        rangeFilter.setLevelMax(level);
        rangeFilter.setLevelMin(level);
        rollingAppender.addFilter(rangeFilter);
//        if (!name.equals(PackageFilter.QUARTZ_LOGGER_NAME)) {
//            // Ajoute un filtre pour exclure org.quartz de l'appender.
//            // Supprime quartz de l'appender.
//            PackageFilter packFilter = new PackageFilter();
//            rollingAppender.addFilter(packFilter);
//        }
        
        rollingAppender.setName(name);
        rollingAppender.setFile(System.getProperty("user.home") + "/Library/lightserverdemo/data/" + filename);
        rollingAppender.setLayout(new PatternLayout(pattern));
        rollingAppender.setMaxFileSize(maxFileSize);
        rollingAppender.setMaxBackupIndex(maxBackupIndex);
        rollingAppender.setEncoding("UTF8");
        rollingAppender.setThreshold(level);
        rollingAppender.setAppend(true);
        rollingAppender.activateOptions();
        
        
        return rollingAppender;
    }
    
    

}

