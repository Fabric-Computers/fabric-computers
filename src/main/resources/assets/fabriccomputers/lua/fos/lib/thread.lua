-- Heavily inspired by https://github.com/cc-tweaked/CC-Tweaked/blob/mc-1.16.x/src/main/resources/data/computercraft/lua/rom/apis/parallel.lua

local thread = {}

local function create(...)
    local functions = table.pack(...)
    local coroutines = {}

    for i = 1, functions.n do
        coroutines[i] = coroutine.create(functions[i])
    end

    return coroutines
end

local function run(coroutines, limit)
    local count = #coroutines
    local living = count

    local filters = {}
    local event = {}

    while true do
        for i = 1, count do
            local c = coroutines[i]

            if c then
                if filters[c] == nil or filters[c] == event[1] then
                    local ok, param = coroutine.resume(c, event)

                    if ok then
                        filters[c] = param
                    else
                        error(param, 0)
                    end

                    if coroutine.status(c) == "dead" then
                        coroutines[i] = nil
                        living = living - 1

                        if living <= limit then
                            return
                        end
                    end
                end
            end
        end

        event = computer:pollEvent()
    end
end

function thread.waitForAny(...)
    local coroutines = create(...)
    run(coroutines, #coroutines - 1)
end

function thread.waitForAll(...)
    local coroutines = create(...)
    run(coroutines, 0)
end

function thread.pollEvent(name)
    return coroutine.yield(name)
end

return thread