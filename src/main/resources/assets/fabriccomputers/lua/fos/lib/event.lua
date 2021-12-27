event = {}

function event.pollEvents()
    local event = computer:pollEvent()
    while not event do
        event = computer:pollEvent()
    end

    return table.unpack(event)
end

function event.queueEvent(name, ...)
    computer:queueEvent(name, {...})
end