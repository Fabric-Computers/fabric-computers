function loadLibrary(lib)
    local content = _G.fileSystems["storage0"]:readFile("lib/"..lib..".lua")
    local func, err = load(content)
    if func then
        local ok, i = pcall(func)
        if not ok then
            print("Cannot load library: ", i)
        end
    else
        print("Cannot load library: ", err)
    end
end

loadLibrary("io")
loadLibrary("event")
loadLibrary("thread")

_G.print = io.print

thread.create(function()
    while true do
        local name, key = event.pollEvents()
        if name == "interrupt" then
            break
        elseif name == "key_down" then
            print("Key Pressed2!")
        end
    end

end)

thread.create(function()
    while true do
        local name, key = event.pollEvents()
        if name == "interrupt" then
            break
        elseif name == "key_down" then
            print("Key Pressed1!")
        end
    end

end)

thread.waitForAll()