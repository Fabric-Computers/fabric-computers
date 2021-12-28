local thread = {}

thread.threads = {}
thread.sleepTimer = 0
thread.done = false

function thread.create(func)
    if func then
        table.insert(thread.threads, coroutine.create(func))
    end
end

function thread.sleep(seconds)
    thread.sleepTimer = seconds * 20
    local cur = 0
    while cur < sleepTimer do cur = cur + 1 end
end

function thread.waitForAll()
    if #thread.threads == 0 then
        thread.done = true
    end
    while not thread.done do
        for i, t in pairs(thread.threads) do
            if coroutine.status(t) == "dead" then
                table.remove(thread.threads, i)
            else
                done = false
                coroutine.resume(t)
            end
        end
    end
end

function thread.waitForAny()
    if #thread.threads == 0 then
        thread.done = true
    end
    while not thread.done do
        for i, t in pairs(thread.threads) do
            if coroutine.status(t) == "dead" then
                table.remove(thread.threads, i)
                thread.done = true
                thread.threads = {}
                print("Done!")
                break
            else
                done = false
                coroutine.resume(t)
            end
        end
    end
end

return thread