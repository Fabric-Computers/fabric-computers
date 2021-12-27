local event = {}

function event.pollEvents()
    local ev = computer:pollEvent()
    while not ev do
        ev = computer:pollEvent()
    end

    return table.unpack(ev)
end

function event.queueEvent(name, ...)
    computer:queueEvent(name, {...})
end

return event