import org.quartz.core.QuartzScheduler

quartz {
    autoStartup = false
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = false
    makeSchedulerThreadDaemon = true
    makeThreadsDaemons = true
}