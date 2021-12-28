local io = os.loadLibrary("io")

if io.screen then
    local size = io.screen:getScreenSize()
    for x=0, size[1] do
        for y=0, size[2] do
            io.setPixel(x, y, color)
        end
    end
end
io.currentLine = 0